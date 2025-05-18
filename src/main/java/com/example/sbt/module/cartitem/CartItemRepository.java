package com.example.sbt.module.cartitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    void deleteAllByCartId(UUID cartId);

    Optional<CartItem> findTopByCartIdAndProductId(UUID cartId, UUID productId);

}
