package com.company.orderplatform.customerManagement.service;


import com.company.orderplatform.common.exceptions.IlleagelAction;
import com.company.orderplatform.common.exceptions.NotFoundException;
import com.company.orderplatform.customerManagement.dto.CustomerRequestDto;
import com.company.orderplatform.customerManagement.entity.Customer;
import com.company.orderplatform.customerManagement.entity.CustomerStatus;
import com.company.orderplatform.customerManagement.mapper.CustomerMapper;
import com.company.orderplatform.customerManagement.repository.CustomerRepository;
import com.company.orderplatform.customerManagement.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerServiceImpl customerService;


/*

    public Customer createCustomer(CustomerRequestDto requestDto);
    public Customer getCustomerById(Long customerId);
    public Page<Customer> getAllCustomerPaginated(Pageable pageable);
    public Customer updateCustomer(Long customerId ,  CustomerRequestDto customerRequestDto);
 */

    @Test
    void shouldReturnCustomerWhenExist(){
        System.out.println("shouldReturnCustomerWhenExist");
        Customer c =    Customer.builder()
                .name("test 1")
                .status(CustomerStatus.ACTIVE)
                .email(".b@c.com")
                .build();    // new Customer();
        c.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(c));

        Customer c1 = customerService.getCustomerById(1L);

        System.out.println(c1.getId());

        assertEquals(1L , c1.getId());
        System.out.println("shouldReturnCustomerWhenExist");


        verify(customerRepository).findById(1L);


    }


    @Test
    void shouldThrowExceptionWhenNotExist(){
        System.out.println("shouldReturnCustomerWhenExist");
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class ,
                ()->{ customerService.getCustomerById(1L);}
                );

    }

    @Test
    void shouldCreateCustomer(){
        CustomerRequestDto customerRequestDto = CustomerRequestDto.builder()
                .name("test c1")
                .email("a.b@c.com")
                .status(CustomerStatus.ACTIVE)
                .build();
        Customer c =    Customer.builder()
                .name("test c1")
                .status(CustomerStatus.ACTIVE)
                .email("a.b@c.com")
                .build();    // new Customer();
        c.setId(1L);

        when(customerRepository.existsByEmail("a.b@c.com")).thenReturn(false);
        when(customerMapper.requestToCustomer(customerRequestDto)).thenReturn(c);
        when(customerRepository.save(c)).thenReturn(c);

        Customer c1 = customerService.createCustomer(customerRequestDto);




    }

    @Test
    void shouldThrowIlleagelActionWhenDuplicateEmailExists(){
        CustomerRequestDto customerRequestDto = CustomerRequestDto.builder()
                .name("test c1")
                .email("a.b@c.com")
                .status(CustomerStatus.ACTIVE)
                .build();

        when(customerRepository.existsByEmail("a.b@c.com")).thenReturn(true);

        assertThrows(IlleagelAction.class , ()->{customerService.createCustomer(customerRequestDto);});

        verify(customerRepository , never()).save(any(Customer.class));


    }

}
