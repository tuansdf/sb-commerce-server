package com.example.sbt.module.cart.service;

import com.example.sbt.module.cart.dto.CartItemDTO;

import java.util.UUID;

public interface CartItemService {

    CartItemDTO save(CartItemDTO requestDTO, UUID userId);

    CartItemDTO setQuantity(CartItemDTO requestDTO, UUID userId);

    CartItemDTO findOneById(UUID id);

    CartItemDTO findOneByIdOrThrow(UUID id);

    void deleteAllByLatestUserCart(UUID userId);

    void deleteAllByCartId(UUID cartId);

    void deleteAllByCartId(UUID cartId, UUID userId);

    void deleteById(UUID id, UUID userId);

}
