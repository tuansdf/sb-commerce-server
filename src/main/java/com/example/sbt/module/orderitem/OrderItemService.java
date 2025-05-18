package com.example.sbt.module.orderitem;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.orderitem.dto.OrderItemDTO;
import com.example.sbt.module.orderitem.dto.SearchOrderItemRequestDTO;

import java.util.UUID;

public interface OrderItemService {

    OrderItemDTO save(OrderItemDTO requestDTO);

    OrderItemDTO findOneById(UUID id);

    OrderItemDTO findOneByIdOrThrow(UUID id);

    PaginationData<OrderItemDTO> search(SearchOrderItemRequestDTO requestDTO, boolean isCount);

}
