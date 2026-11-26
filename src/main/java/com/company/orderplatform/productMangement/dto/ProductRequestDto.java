package com.company.orderplatform.productMangement.dto;

import com.company.orderplatform.productMangement.entity.ProductStatus;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductRequestDto {

/*
    id BIGINT NOT NULL AUTO_INCREMENT,
    sku VARCHAR(80) NOT NULL,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_products_sku UNIQUE (sku)

 */

    @NotBlank(message = "Sku can not be blank")
    @Size(max = 80)
//    @JsonDeserialize(using = StringTrimmerDeserializer.class)
    private String sku;
    @NotBlank(message = "Name can not be blank")
    @Size(max = 200)
    private String name;
    @NotNull(message = "Price can not be blank")
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than zero")
    @Digits(integer = 10 , fraction = 2 , message = "Price must have at most 10 integer digits and 2 decimal places")
    private BigDecimal price;
    @NotNull(message = "Status can not be blank")
    @Builder.Default
    private ProductStatus status = ProductStatus.ACTIVE;

    public String getSku() {
        return sku.trim();
    }

    public String getName() {
        return name.trim();
    }
}
