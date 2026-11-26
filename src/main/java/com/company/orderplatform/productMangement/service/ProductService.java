package com.company.orderplatform.productMangement.service;


import com.company.orderplatform.productMangement.dto.ProductRequestDto;

import com.company.orderplatform.productMangement.entity.Product;
import com.company.orderplatform.productMangement.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {





    public Product createProduct(ProductRequestDto productRequestDto);

    public Product getProductById(Long id);

    public Page<Product> getAllProductPaginated(Pageable pageable , ProductStatus status);

    public Product updateProduct(ProductRequestDto productRequestDto, Long id);
//    public void insertProductCsv(MultipartFile file) throws IOException;
}
