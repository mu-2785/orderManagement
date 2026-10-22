package com.company.orderplatform.customerManagement.service.impl;

import com.company.orderplatform.common.exceptions.IlleagelAction;
import com.company.orderplatform.common.exceptions.NotFoundException;
import com.company.orderplatform.customerManagement.dto.CustomerRequestDto;
import com.company.orderplatform.customerManagement.dto.CustomerResponseDto;
import com.company.orderplatform.customerManagement.entity.Customer;
import com.company.orderplatform.customerManagement.mapper.CustomerMapper;
import com.company.orderplatform.customerManagement.repository.CustomerRepository;
import com.company.orderplatform.customerManagement.service.CustomerService;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final  CustomerMapper customerMapper;
//    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);
    public CustomerServiceImpl(CustomerRepository customerRepository , CustomerMapper customerMapper ){
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;

    }


    @Transactional
    @Override
    public Customer createCustomer(CustomerRequestDto requestDto) {

        System.out.println("createCustomer service");

        if(customerRepository.existsByEmail(requestDto.getEmail())){
            throw new IlleagelAction(String.format("Customer already exists with email %s",requestDto.getEmail()));
        }
/*
        Customer customer = Customer.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .status(requestDto.getStatus())
                .build();
*/
        Customer customer = customerMapper.requestToCustomer(requestDto);


        System.out.println(customer);
        System.out.println(customer.toString());
        return customerRepository.save(customer);

    }

    @Override
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(()-> new NotFoundException(String.format("Customer not found by id %s",customerId)));

    }

    @Override
    public Page<Customer> getAllCustomerPaginated(Pageable pageable) {

        System.out.println(customerRepository.findAll(pageable ));
        return customerRepository.findAll(pageable );
        
    }

    @Override
    @Transactional
    public Customer updateCustomer(Long customerId , CustomerRequestDto customerRequestDto) {
        System.out.println("updateCustomer");
        Customer principalCustomer =  getCustomerById(customerId);


        if(customerRepository.existsByEmailAndIdNot(customerRequestDto.getEmail(), customerId)){
            throw new IlleagelAction(String.format("Customer already exist with email %s", customerRequestDto.getEmail()));
        }

//        Customer updatedCustomer = customerMapper.requestToCustomer(customerRequestDto);
        customerMapper.updateCustomerFromDto(customerRequestDto , principalCustomer);
        return customerRepository.save(principalCustomer);
    }
}
