package com.example.sbt.module.order.service;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.order.dto.OrderItemDTO;
import com.example.sbt.module.order.dto.SearchOrderItemRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class OrderItemServiceImpl implements OrderItemService {

    @Override
    public OrderItemDTO save(OrderItemDTO requestDTO) {
        return null;
    }

    @Override
    public OrderItemDTO findOneById(UUID id) {
        return null;
    }

    @Override
    public OrderItemDTO findOneByIdOrThrow(UUID id) {
        return null;
    }

    @Override
    public PaginationData<OrderItemDTO> search(SearchOrderItemRequestDTO requestDTO, boolean isCount) {
        return null;
    }

}
