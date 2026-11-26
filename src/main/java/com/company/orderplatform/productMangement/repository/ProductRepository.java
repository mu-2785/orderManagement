package com.company.orderplatform.productMangement.repository;

import com.company.orderplatform.productMangement.entity.Product;
import com.company.orderplatform.productMangement.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product , Long> {

    Boolean existsBySku(String sku);
    Page<Product> findAllByStatus(Pageable pageable , ProductStatus status);

    @Query("SELECT DISTINCT p.sku FROM Product p")
    Set<String> getAllSku();
}
