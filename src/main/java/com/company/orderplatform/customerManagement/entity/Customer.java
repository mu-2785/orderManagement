package com.company.orderplatform.customerManagement.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "customers"
)
public class Customer {

    /*
    name VARCHAR(150) NOT NULL,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL,
     */

    @Column(name = "name" , length = 150 , nullable = false)
    private String name;
    @Column(name = "email" , length = 250 , nullable = false , unique = true)
    private String email;
    @Column(name = "status" ,nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CustomerStatus status;


}
