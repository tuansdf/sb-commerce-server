package com.example.sbt.module.order.service;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.order.dto.OrderDTO;
import com.example.sbt.module.order.dto.SearchOrderRequestDTO;

import java.util.UUID;

public interface OrderService {

    OrderDTO save(OrderDTO requestDTO);

    OrderDTO findOneById(UUID id);

    OrderDTO findOneByIdOrThrow(UUID id);

    PaginationData<OrderDTO> search(SearchOrderRequestDTO requestDTO, boolean isCount);

}
