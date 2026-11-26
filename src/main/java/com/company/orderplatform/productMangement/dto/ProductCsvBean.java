package com.company.orderplatform.productMangement.dto;

import com.company.orderplatform.productMangement.entity.ProductStatus;
import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCsvBean {



    @CsvBindByName(column = "sku")
    @NotBlank(message = "SKU can not be blank")
    @Size(max = 80)
    private String sku;
    @CsvBindByName(column = "name")
    @NotBlank(message = "Name can not be blank")
    @Size(max = 200)
    private String name;
    @CsvBindByName(column = "price")
    @NotNull(message = "Price can not be blank")
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than zero")
    @Digits(integer = 10 , fraction = 2 , message = "Price must have at most 10 integer digits and 2 decimal places")
    private BigDecimal price;
    @CsvBindByName(column = "status")
    @NotNull(message = "Status can not be blank")
//    @Builder.Default
    private ProductStatus status ;

}
