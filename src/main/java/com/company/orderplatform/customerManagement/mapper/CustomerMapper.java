package com.company.orderplatform.customerManagement.mapper;


import com.company.orderplatform.customerManagement.dto.CustomerRequestDto;
import com.company.orderplatform.customerManagement.dto.CustomerResponseDto;
import com.company.orderplatform.customerManagement.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponseDto customerToResponse(Customer customer);
    Customer requestToCustomer(CustomerRequestDto requestDto);

    void updateCustomerFromDto(CustomerRequestDto customerRequestDto , @MappingTarget Customer customer);
}
