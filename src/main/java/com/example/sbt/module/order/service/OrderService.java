package com.example.sbt.module.order.service;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.order.dto.OrderDTO;
import com.example.sbt.module.order.dto.SearchOrderRequestDTO;

import java.util.UUID;

public interface OrderService {

    OrderDTO createByUserId(UUID userId);

    OrderDTO findOneById(UUID id, UUID userId);

    OrderDTO findOneByIdOrThrow(UUID id, UUID userId);

    PaginationData<OrderDTO> search(SearchOrderRequestDTO requestDTO, boolean isCount);

}
