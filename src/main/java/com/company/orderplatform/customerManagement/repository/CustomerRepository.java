package com.company.orderplatform.customerManagement.repository;

import com.company.orderplatform.customerManagement.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer , Long> {
    Boolean existsByEmail(String email);
    Boolean existsByEmailAndIdNot (String email , Long id);
}
