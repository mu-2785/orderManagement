package com.company.orderplatform.customerManagement.controller;

/*

import com.company.orderplatform.customerManagement.entity.Customer;
import com.company.orderplatform.customerManagement.entity.CustomerStatus;
import com.company.orderplatform.customerManagement.mapper.CustomerMapper;
import com.company.orderplatform.customerManagement.service.CustomerService;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

 */

import com.company.orderplatform.customerManagement.dto.CustomerResponseDto;
import com.company.orderplatform.customerManagement.entity.Customer;
import com.company.orderplatform.customerManagement.entity.CustomerStatus;
import com.company.orderplatform.customerManagement.mapper.CustomerMapper;
import com.company.orderplatform.customerManagement.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest                         //  create limited application context (controller only )
class CustomerControllerTest {


    @Autowired
    private MockMvc mockMvc;            //  to mock api call without turning up server and port number

    @MockitoBean                        //  mockito will create bean and put in to application context , to mock actual flow
    private CustomerService customerService;

    @MockitoBean
    private CustomerMapper customerMapper;

    @MockitoBean(name = "jpaMappingContext")
    private JpaMetamodelMappingContext jpaMappingContext;


    @Test
    void shouldReturnCustomerIfExists() throws Exception {
        Customer c =    Customer.builder()
                .name("test 1")
                .status(CustomerStatus.ACTIVE)
                .email(".b@c.com")
                .build();    // new Customer();
        c.setId(1L);

        CustomerResponseDto response = CustomerResponseDto.builder()
                .id(1L)
                .name("test 1")
                .email("a.b@c.com")
                .status(CustomerStatus.ACTIVE)
                .build();

        when(customerMapper.customerToResponse(any(Customer.class)))
                .thenReturn(response);
        when(customerService.getCustomerById(1L)).thenReturn(c);

        mockMvc.perform(get("/api/v1/customers/{id}",1L)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("test 1"));

    }


}