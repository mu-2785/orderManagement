package com.company.orderplatform.customerManagement.controller;

import com.company.orderplatform.customerManagement.dto.CustomerRequestDto;
import com.company.orderplatform.customerManagement.dto.CustomerResponseDto;
import com.company.orderplatform.customerManagement.entity.Customer;
import com.company.orderplatform.customerManagement.mapper.CustomerMapper;
import com.company.orderplatform.customerManagement.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.websocket.server.PathParam;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    /*
    API endpoints
Method	Endpoint            	Expected behavior
POST	/api/v1/customers	    Create a customer
GET 	/api/v1/customers/{id}	Get a customer by ID
GET	    /api/v1/customers	    Get a paginated customer list
PUT	    /api/v1/customers/{id}	Update a customer's details
     */

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    public CustomerController(CustomerService customerService , CustomerMapper customerMapper){
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody @Valid CustomerRequestDto requestDto){
        System.out.println("createCustomer");
        System.out.println(requestDto.toString());

        CustomerResponseDto response =  customerMapper.customerToResponse(customerService.createCustomer(requestDto));
        //return response;

        return ResponseEntity.status(201).body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id){
        CustomerResponseDto responseDto = customerMapper.customerToResponse(customerService.getCustomerById(id));
        return ResponseEntity.status(200).body(responseDto);
    }
    @GetMapping
    public Page<CustomerResponseDto> getAllCustomerPaginated(@RequestParam(required = false , defaultValue = "0") @Min(0) int page ,
                                        @RequestParam(required = false , defaultValue = "10") @Min(10) @Max(100) int size,
                                        @RequestParam(required = false , defaultValue = "name") String sortBy,
                                        @RequestParam(required = false , defaultValue = "ASC") String sortDirection
    ){

        Sort sort = null;
        if(sortDirection.equalsIgnoreCase("ASC")){
            sort = Sort.by(sortBy).ascending();
        }
        else{
            sort = Sort.by(sortBy).descending();
        }
        Pageable pageable = PageRequest.of(page, size , sort);
//        return customerService.getAllCustomerPaginated(pageable );

         return customerService.getAllCustomerPaginated(pageable ).map(c ->customerMapper.customerToResponse(c));

    }
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable Long id , @RequestBody @Valid CustomerRequestDto customerRequestDto){
        CustomerResponseDto responseDto = customerMapper.customerToResponse(customerService.updateCustomer(id , customerRequestDto));

        return ResponseEntity.status(200).body(responseDto);

    }

}
