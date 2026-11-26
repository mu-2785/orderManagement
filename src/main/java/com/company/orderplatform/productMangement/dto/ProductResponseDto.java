package com.company.orderplatform.productMangement.dto;


import com.company.orderplatform.productMangement.entity.ProductStatus;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductResponseDto {


    private Long id;
    private String sku;
    private String name;
    private BigDecimal price;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
