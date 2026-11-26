package com.company.orderplatform.productMangement.service;


import com.company.orderplatform.productMangement.dto.ProductCsvBean;
import com.company.orderplatform.productMangement.dto.ProductImportErrorDto;
import com.company.orderplatform.productMangement.dto.ProductImportResponseDto;
import com.company.orderplatform.productMangement.entity.Product;
import com.company.orderplatform.productMangement.entity.ProductStatus;
import com.company.orderplatform.productMangement.repository.ProductRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImportService {

    private final ProductRepository productRepository;

    // Spring injects the Bean Validation implementation.
    private final Validator validator;


    @Transactional
    public ProductImportResponseDto insertProductCsv(MultipartFile file)
            throws IOException {

        // STEP 1: Validate uploaded file.
        validateFile(file);


        // STEP 2: Parse CSV into Java DTO objects.
        List<ProductCsvBean> csvRecords = parseCsv(file);


        // STEP 3: Prepare collections for accepted products and errors.
        List<Product> validProducts = new ArrayList<>();

        List<ProductImportErrorDto> errors = new ArrayList<>();


        // STEP 4: Track SKUs already encountered in this import.
        // This prevents duplicates within the same CSV.
        Set<String> processedSkus = new HashSet<>();


        // STEP 5: Fetch existing SKUs from the database.
        // This is acceptable for our small learning dataset.
        // We'll optimize it later.
        Set<String> existingSkus = productRepository.getAllSku()
                .stream()
                .map(this::normalizeSku)
                .collect(Collectors.toSet());


        // STEP 6: Process each CSV record individually.
        for (int i = 0; i < csvRecords.size(); i++) {

            ProductCsvBean csvLine = csvRecords.get(i);


            // Header is row 1, so first data record is row 2.
            int rowNumber = i + 2;


            // STEP 7: Apply defaults before validation.
            if (csvLine.getStatus() == null) {
                csvLine.setStatus(ProductStatus.ACTIVE);
            }


            // STEP 8: Collect validation errors.
            List<ProductImportErrorDto> rowErrors =
                    validateProductCsvBean(csvLine, rowNumber);


            // STEP 9: Check SKU duplicates.
            String sku = normalizeSku(csvLine.getSku());

            // Only check duplicates if SKU is nonblank.
            if (sku != null) {

                // Set.add() returns false if SKU already exists.
                if (existingSkus.contains(sku)) {

                    rowErrors.add(
                            createError(
                                    rowNumber,
                                    "sku",
                                    "SKU already exists in database"
                            )
                    );

                } else if (!processedSkus.add(sku)) {

                    rowErrors.add(
                            createError(
                                    rowNumber,
                                    "sku",
                                    "Duplicate SKU in uploaded CSV"
                            )
                    );
                }
            }


            // STEP 10: Reject the entire row if any errors exist.
            if (!rowErrors.isEmpty()) {

                errors.addAll(rowErrors);

                // continue skips the entity mapping below
                // and moves to the next CSV record.
                continue;
            }


            // STEP 11: Convert validated CSV DTO into Product entity.
            Product product = Product.builder()
                    .sku(sku)
                    .name(csvLine.getName().trim())
                    .price(csvLine.getPrice())
                    .status(csvLine.getStatus())
                    .build();


            // STEP 12: Store the valid entity in our collection.
            validProducts.add(product);
        }


        // STEP 13: Save only accepted products.
        if (!validProducts.isEmpty()) {
            productRepository.saveAll(validProducts);
        }


        // STEP 14: Prepare final import report.
        int totalRecords = csvRecords.size();

        int successCount = validProducts.size();

        int failureCount = totalRecords - successCount;


        return ProductImportResponseDto.builder()
                .totalRecords(totalRecords)
                .successCount(successCount)
                .failureCount(failureCount)
                .errors(errors)
                .build();
    }






    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Uploaded CSV file cannot be empty"
            );
        }

        // 5 MB limit for our first implementation.
        // The application configuration must also allow this size.
        long maxFileSize = 5 * 1024 * 1024;

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException(
                    "CSV file cannot exceed 5 MB"
            );
        }

        String filename = file.getOriginalFilename();

        if (filename == null ||
                !filename.toLowerCase(Locale.ROOT).endsWith(".csv")) {

            throw new IllegalArgumentException(
                    "Only CSV files are supported"
            );
        }
    }

    private List<ProductCsvBean> parseCsv(MultipartFile file)
            throws IOException {

        try (Reader reader = new BufferedReader( new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8 ))) {

            // This strategy maps columns using header names.
            // Example: "sku" -> ProductCsvBean.sku
            HeaderColumnNameMappingStrategy<ProductCsvBean> strategy =  new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ProductCsvBean.class);


            CsvToBean<ProductCsvBean> csvToBean = new CsvToBeanBuilder<ProductCsvBean>(reader)
                                                    .withMappingStrategy(strategy)
                                                    .withIgnoreEmptyLine(true)
                                                    .withIgnoreLeadingWhiteSpace(true)
                                                    .withThrowExceptions(false)
                                                    .build();


            // parse() converts CSV rows into ProductCsvBean objects.
            List<ProductCsvBean> records = csvToBean.parse();


            // IMPORTANT:
            // withThrowExceptions(false) does not mean parsing errors
            // disappear. We must explicitly inspect captured exceptions.
            if (!csvToBean.getCapturedExceptions().isEmpty()) {

                throw new IllegalArgumentException(
                        "CSV contains malformed or invalidly formatted data. " +
                                "Parsing failed for " +
                                csvToBean.getCapturedExceptions().size() +
                                " record(s)."
                );
            }

            return records;
        }
    }


    private List<ProductImportErrorDto> validateProductCsvBean(
            ProductCsvBean csvLine,
            int rowNumber
    ) {

        List<ProductImportErrorDto> errors = new ArrayList<>();


        // Spring Validator checks the annotations on ProductCsvBean.
        Set<ConstraintViolation<ProductCsvBean>> violations =
                validator.validate(csvLine);


        // Convert Bean Validation violations into our own error DTO.
        for (ConstraintViolation<ProductCsvBean> violation : violations) {

            errors.add(
                    createError(
                            rowNumber,
                            violation.getPropertyPath().toString(),
                            violation.getMessage()
                    )
            );
        }

        return errors;
    }

    private String normalizeSku(String sku) {

        if (sku == null || sku.isBlank()) {
            return null;
        }

        // Treat p001, P001 and " P001 " as the same SKU.
        return sku.trim().toUpperCase(Locale.ROOT);
    }

    private ProductImportErrorDto createError(
            int rowNumber,
            String field,
            String message
    ) {

        return ProductImportErrorDto.builder()
                .rowNumber(rowNumber)
                .field(field)
                .message(message)
                .build();
    }




}