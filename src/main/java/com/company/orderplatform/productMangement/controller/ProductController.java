package com.company.orderplatform.productMangement.controller;


import com.company.orderplatform.common.exceptions.InvalidArgumentException;
import com.company.orderplatform.productMangement.dto.ProductImportResponseDto;
import com.company.orderplatform.productMangement.dto.ProductRequestDto;
import com.company.orderplatform.productMangement.dto.ProductResponseDto;
import com.company.orderplatform.productMangement.entity.ProductStatus;
import com.company.orderplatform.productMangement.mapper.ProductMapper;
import com.company.orderplatform.productMangement.service.ProductImportService;
import com.company.orderplatform.productMangement.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/products")
@Validated


public class ProductController {

    /*
        Base path: /api/v1/products
        Method	    Endpoint            	Description
        POST	    /api/v1/products	    Create a product
        GET	        /api/v1/products/{id}	Retrieve one product
        GET	        /api/v1/products	    Retrieve a paginated product list
        PUT	        /api/v1/products/{id}	Update a product's details
     */

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductImportService productImportService;
    public ProductController(ProductService productService , ProductMapper productMapper , ProductImportService productImportService){
        this.productService = productService;
        this.productMapper = productMapper;
        this.productImportService = productImportService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto productRequestDto){
        ProductResponseDto productResponseDto = productMapper.productToResponseDto(productService.createProduct(productRequestDto));
        return ResponseEntity.status(201).body(productResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id){
        ProductResponseDto productResponseDto = productMapper.productToResponseDto(productService.getProductById(id));
        return ResponseEntity.status(200).body(productResponseDto);
    }

    @GetMapping
    public Page<ProductResponseDto> getAllProductPaginated(
            @Valid
            @RequestParam(required = false ,defaultValue = "0")@Min(value = 0) int pageNo,
            @RequestParam(required = false ,defaultValue = "10")@Min(value = 0) @Max(value = 20)  int pageSize,
            @RequestParam(required = false ,defaultValue = "asc") String sortDirection,
            @RequestParam(required = false ,defaultValue = "name") String sortBy,
            @RequestParam(required = false )ProductStatus status
            ){
        Set<String> allowedSortBy = Set.of("name", "sku", "price", "createdAt");
        Sort sort = null;
        if(!allowedSortBy.contains(sortBy)){
            throw new InvalidArgumentException(String.format("Invalid Sort field %s",sortBy));
        }
        if(sortDirection.equalsIgnoreCase("ASC")){
            sort = Sort.by(sortBy).ascending();
        }
        else if(sortDirection.equalsIgnoreCase("desc")){
            sort = Sort.by(sortBy).descending();
        }
        else {
            throw new InvalidArgumentException(String.format("Invalid Sort direction %s",sortDirection));
        }

        Pageable pageable = PageRequest.of(pageNo , pageSize , sort);
        return productService.getAllProductPaginated(pageable, status).map(p->productMapper.productToResponseDto(p));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id , @RequestBody @Valid ProductRequestDto productRequestDto){
        ProductResponseDto productResponseDto = productMapper.productToResponseDto(productService.updateProduct(productRequestDto,id));
        return ResponseEntity.status(200).body(productResponseDto);
    }

    @PostMapping(
            value = "/import",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProductImportResponseDto> importProducts(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        ProductImportResponseDto response =
                productImportService.insertProductCsv(file);

        return ResponseEntity.ok(response);
    }


}
