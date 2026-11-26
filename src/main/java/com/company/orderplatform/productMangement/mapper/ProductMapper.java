package com.company.orderplatform.productMangement.mapper;


import com.company.orderplatform.productMangement.dto.ProductRequestDto;
import com.company.orderplatform.productMangement.dto.ProductResponseDto;
import com.company.orderplatform.productMangement.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product requestToProduct(ProductRequestDto productRequestDto);
    ProductResponseDto productToResponseDto(Product product);
    void updateProductFromDto(ProductRequestDto productRequestDto , @MappingTarget Product product);

}
