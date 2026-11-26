package com.company.orderplatform.productMangement.service.impl;

import com.company.orderplatform.common.exceptions.IlleagelAction;
import com.company.orderplatform.common.exceptions.NotFoundException;
import com.company.orderplatform.productMangement.dto.ProductCsvBean;
import com.company.orderplatform.productMangement.dto.ProductRequestDto;
import com.company.orderplatform.productMangement.entity.Product;
import com.company.orderplatform.productMangement.entity.ProductStatus;
import com.company.orderplatform.productMangement.mapper.ProductMapper;
import com.company.orderplatform.productMangement.repository.ProductRepository;
import com.company.orderplatform.productMangement.service.ProductService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final Validator validator;

    public ProductServiceImpl(ProductRepository productRepository , ProductMapper productMapper , Validator validator){
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.validator = validator;
    }


    @Override
    public Product createProduct(ProductRequestDto productRequestDto) {
        if(productRepository.existsBySku(productRequestDto.getSku())){
            throw new IlleagelAction(String.format("Product already exist by SKU : %s", productRequestDto.getSku()));
        }

        Product newProduct = productMapper.requestToProduct(productRequestDto);

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new NotFoundException(String.format("Product not found by ID : %s",id)));
    }

    @Override
    public Page<Product> getAllProductPaginated(Pageable pageable , ProductStatus status) {
        if(status!=null ){
            return productRepository.findAllByStatus(pageable , status);
        }
        return productRepository.findAll(pageable);
    }

    @Override
    public Product updateProduct(ProductRequestDto productRequestDto , Long id) {
        Product currentProduct = productRepository.findById(id).orElseThrow(()->new NotFoundException(String.format("Product not found by ID : %s",id)));
        if(!(currentProduct.getSku().equalsIgnoreCase(productRequestDto.getSku())) && productRepository.existsBySku(productRequestDto.getSku()) ){
            throw new IlleagelAction(String.format("Product with SKU %s already exists" , productRequestDto.getSku()));
        }
        productMapper.updateProductFromDto(productRequestDto , currentProduct);

        return productRepository.save(currentProduct);
    }

    /*
    public void insertProductCsv(MultipartFile file) throws IOException{

        if(file.isEmpty()){
            throw new IlleagelAction(String.format("File seems to be empty..."));
        }

        List<Product> parsedProductList = parseCsv(file);
        productRepository.saveAll(parsedProductList);
    }

    private List<Product> parseCsv(MultipartFile file) throws IOException {

        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){

            //  set strategy
            HeaderColumnNameMappingStrategy strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ProductCsvBean.class);

            CsvToBean<ProductCsvBean> csvToBean = new CsvToBeanBuilder<ProductCsvBean>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(false)
                    .build();

            Set<String> processedSkus = new HashSet<>();
            processedSkus = productRepository.getAllSku();


            Set<String> finalProcessedSkus = processedSkus;
            return csvToBean.parse().stream().map(csvLine ->{
                Set<ConstraintViolation<ProductCsvBean>> violations = validator.validate(csvLine);

                if (!finalProcessedSkus.add(csvLine.getSku().toLowerCase().trim())) {
                    // violations.add() Duplicate email in the uploaded CSV
                }
                if (!violations.isEmpty()) {

                    for (ConstraintViolation<ProductCsvBean> violation : violations) {
                        System.out.println(violation.getPropertyPath() + " : " + violation.getMessage());
                    }
                }

               return Product.builder()
                        .name(csvLine.getName())
                        .sku(csvLine.getSku())
                        .price(csvLine.getPrice())
                        .status(csvLine.getStatus())
                        .build();
            }).toList();

        }

    }
*/

}
