package com.example.sbt.module.cart;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.cart.dto.CartDTO;
import com.example.sbt.module.cart.dto.SearchCartRequestDTO;

import java.util.UUID;

public interface CartService {

    CartDTO init(UUID userId);

    void deleteById(UUID cartId, UUID userId);

    CartDTO findOneLatestByUserId(UUID userId);

    CartDTO findOneOrInitByUserId(UUID userId);

    CartDTO findOneById(UUID cartId, UUID userId);

    CartDTO findOneByIdOrThrow(UUID cartId, UUID userId);

    PaginationData<CartDTO> search(SearchCartRequestDTO requestDTO, boolean isCount);

}
