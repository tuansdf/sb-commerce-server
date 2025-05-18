package com.example.sbt.module.cart;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.cart.dto.CartDTO;
import com.example.sbt.module.cart.dto.SearchCartRequestDTO;

import java.util.UUID;

public interface CartService {

    CartDTO save(CartDTO requestDTO);

    CartDTO findOneById(UUID id);

    CartDTO findOneByIdOrThrow(UUID id);

    PaginationData<CartDTO> search(SearchCartRequestDTO requestDTO, boolean isCount);

}
