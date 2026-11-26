package com.company.orderplatform.productMangement.dto;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImportErrorDto {

    // Actual CSV row number, including the header row.
    private Integer rowNumber;

    // Example: sku, name, price, status.
    private String field;

    // Human-readable reason for rejection.
    private String message;
}
