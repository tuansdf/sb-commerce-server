package com.example.sbt.module.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value = "select p.stock from product p where p.id = :productId limit 1", nativeQuery = true)
    Integer findStockByProductId(UUID productId);

}
