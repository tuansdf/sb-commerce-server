package com.example.sbt.module.cart.repository;

import com.example.sbt.module.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    void deleteAllByCartId(UUID cartId);

    Optional<CartItem> findTopByCartIdAndProductId(UUID cartId, UUID productId);

    List<CartItem> findAllByCartId(UUID cartId);

    @Query(value = "select ci.* from cart_item ci left join cart c on (c.id = ci.cart_id) where ci.id = :itemId and c.user_id = :userId limit 1", nativeQuery = true)
    Optional<CartItem> findTopByIdAndUserId(UUID itemId, UUID userId);

}
