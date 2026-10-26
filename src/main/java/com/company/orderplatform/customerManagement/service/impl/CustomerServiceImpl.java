package com.company.orderplatform.customerManagement.service.impl;

import com.company.orderplatform.common.exceptions.IlleagelAction;
import com.company.orderplatform.common.exceptions.NotFoundException;
import com.company.orderplatform.customerManagement.dto.CustomerCsvBean;
import com.company.orderplatform.customerManagement.dto.CustomerRequestDto;
import com.company.orderplatform.customerManagement.dto.CustomerResponseDto;
import com.company.orderplatform.customerManagement.entity.Customer;
import com.company.orderplatform.customerManagement.entity.CustomerStatus;
import com.company.orderplatform.customerManagement.mapper.CustomerMapper;
import com.company.orderplatform.customerManagement.repository.CustomerRepository;
import com.company.orderplatform.customerManagement.service.CustomerService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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


    public String handleFileUpload(MultipartFile file) throws IOException  {

        System.out.println("handleFileUpload");
        long start = System.nanoTime();
        System.out.println("start  "+start);

        Set<Customer> customerSet = parseCsv(file);
        long afterParsing = System.nanoTime();
        System.out.println("afterParsing  "+afterParsing);

        customerRepository.saveAll(customerSet);
        long afterSave = System.nanoTime();
        System.out.println("afterSave  "+afterSave);



        return "customerRepository.saveAll(customerSet); "+customerSet.size();
    }

    private Set<Customer> parseCsv(MultipartFile file) throws IOException {

        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream())))
        {
            //      create strategy how to map fields
            HeaderColumnNameMappingStrategy<CustomerCsvBean> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(CustomerCsvBean.class);

            //      set required properties
            CsvToBean<CustomerCsvBean> csvToBean = new CsvToBeanBuilder<CustomerCsvBean>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(false)   // collect errors instead of failing fast
                    .build();

            //      now parse
            return csvToBean.parse()
                    .stream()
                    .map(csvLine ->{
//                        System.out.println(csvLine);
                        return Customer.builder()
                                .name(csvLine.getName())
                                .email(csvLine.getEmail())
//                                .status(CustomerStatus.valueOf(csvLine.getStatus()))
                                .status(csvLine.getStatus())
                                .build();

                    }).collect(Collectors.toSet());

        }
    }

    @Transactional
    public String handleFileUploadDelete(MultipartFile file){

        List<String> listOfEmails = new ArrayList<>();

        listOfEmails = extractEmailsFromCsv(file);

        customerRepository.deleteAllByEmailIn(listOfEmails);

        return "";
    }

    private List<String> extractEmailsFromCsv(MultipartFile file){

        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream())))
        {
            HeaderColumnNameMappingStrategy<CustomerCsvBean> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(CustomerCsvBean.class);

            CsvToBean<CustomerCsvBean> csvToBean = new  CsvToBeanBuilder<CustomerCsvBean>(reader)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withMappingStrategy(strategy)
                    .build();

            return csvToBean.parse()
                    .stream()
                    .map(csvLine ->{
                        return csvLine.getEmail();
                    }).toList();

        }
        catch (Exception e){
            System.out.println("extractEmailsFromCsv Exception");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println(e.getStackTrace());

            return null;
        }


    }
}
