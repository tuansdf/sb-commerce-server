package com.example.sbt.module.cart.service;

import com.example.sbt.module.cart.dto.CartDTO;
import com.example.sbt.module.cart.dto.CartItemDTO;

import java.util.UUID;

public interface CartService {

    CartDTO findOneOrInitByUserId(UUID userId);

    void deleteAllItemsByUserId(UUID userId);

    void deleteItemByIdAndUserId(UUID itemId, UUID userId);

    CartItemDTO setItemQuantity(CartItemDTO requestDTO, UUID userId);

    CartItemDTO addItem(CartItemDTO requestDTO, UUID userId);

}
