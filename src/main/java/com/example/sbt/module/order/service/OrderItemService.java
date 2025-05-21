package com.example.sbt.module.order.service;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.order.dto.OrderItemDTO;
import com.example.sbt.module.order.dto.SearchOrderItemRequestDTO;

import java.util.UUID;

public interface OrderItemService {

    OrderItemDTO save(OrderItemDTO requestDTO);

    OrderItemDTO findOneById(UUID id);

    OrderItemDTO findOneByIdOrThrow(UUID id);

    PaginationData<OrderItemDTO> search(SearchOrderItemRequestDTO requestDTO, boolean isCount);

}
