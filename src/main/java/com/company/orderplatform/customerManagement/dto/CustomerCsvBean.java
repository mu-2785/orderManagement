package com.company.orderplatform.customerManagement.dto;

import com.company.orderplatform.customerManagement.entity.CustomerStatus;
import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class CustomerCsvBean {

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "status")
    private CustomerStatus status;
}
