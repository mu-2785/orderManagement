package com.company.orderplatform.productMangement.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImportResponseDto {

    private int totalRecords;

    private int successCount;

    private int failureCount;

    private List<ProductImportErrorDto> errors;
}
