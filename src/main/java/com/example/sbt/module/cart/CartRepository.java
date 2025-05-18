package com.example.sbt.module.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    Boolean existsByUserId(UUID userId);

    void deleteByIdAndUserId(UUID cartId, UUID userId);

    Optional<Cart> findTopByIdAndUserId(UUID cartId, UUID userId);

    Optional<Cart> findTopByUserIdOrderByCreatedAtDesc(UUID userId);

}
