package com.example.sbt.module.cartitem;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.cartitem.dto.CartItemDTO;
import com.example.sbt.module.cartitem.dto.SearchCartItemRequestDTO;

import java.util.UUID;

public interface CartItemService {

    CartItemDTO save(CartItemDTO requestDTO);

    CartItemDTO findOneById(UUID id);

    CartItemDTO findOneByIdOrThrow(UUID id);

    PaginationData<CartItemDTO> search(SearchCartItemRequestDTO requestDTO, boolean isCount);

}
