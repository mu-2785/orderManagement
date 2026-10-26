package com.company.orderplatform.customerManagement.service;

import com.company.orderplatform.customerManagement.dto.CustomerRequestDto;
import com.company.orderplatform.customerManagement.dto.CustomerResponseDto;
import com.company.orderplatform.customerManagement.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface CustomerService {


    public Customer createCustomer(CustomerRequestDto requestDto);
    public Customer getCustomerById(Long customerId);
    public Page<Customer> getAllCustomerPaginated(Pageable pageable);
    public Customer updateCustomer(Long customerId ,  CustomerRequestDto customerRequestDto);
    public String handleFileUpload(MultipartFile file) throws IOException;
    public String handleFileUploadDelete(MultipartFile file) throws IOException;

}
