package com.example.sbt.module.cartitem;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.cartitem.dto.CartItemDTO;
import com.example.sbt.module.cartitem.dto.SearchCartItemRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class CartItemServiceImpl implements CartItemService {

    @Override
    public CartItemDTO save(CartItemDTO requestDTO) {
        return null;
    }

    @Override
    public CartItemDTO findOneById(UUID id) {
        return null;
    }

    @Override
    public CartItemDTO findOneByIdOrThrow(UUID id) {
        return null;
    }

    @Override
    public PaginationData<CartItemDTO> search(SearchCartItemRequestDTO requestDTO, boolean isCount) {
        return null;
    }

}
