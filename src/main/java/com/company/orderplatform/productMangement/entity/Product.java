package com.company.orderplatform.productMangement.entity;


import com.company.orderplatform.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_products_sku" , columnNames = {"sku"} )
        },
        indexes = {
                @Index(name = "idx_products_sku" , columnList = "sku"),
                @Index(name = "idx_products_name" , columnList = "name")
        }
)
public class Product extends BaseEntity {
/*
    id BIGINT NOT NULL AUTO_INCREMENT,
    sku VARCHAR(80) NOT NULL,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uk_products_sku UNIQUE (sku)

 */

    @Column(name = "sku" , length = 80 , nullable = false , unique = true)
    private String sku;
    @Column(name = "name" , length = 200 , nullable = false , unique = false)
    private String name;
    @Column(name = "price" ,precision = 12 , scale = 2 , nullable = false)
    private BigDecimal price;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status" , length = 30 , nullable = false , unique = false)
    private ProductStatus status = ProductStatus.ACTIVE;






}
