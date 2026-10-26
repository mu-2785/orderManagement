package com.company.orderplatform.customerManagement.entity;


import com.company.orderplatform.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "customers"
)
@Builder
public class Customer extends BaseEntity {

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
    private CustomerStatus status = CustomerStatus.ACTIVE ;


    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}
