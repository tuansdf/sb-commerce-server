package com.example.sbt.module.order;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.order.dto.OrderDTO;
import com.example.sbt.module.order.dto.SearchOrderRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class OrderServiceImpl implements OrderService {

    @Override
    public OrderDTO save(OrderDTO requestDTO) {
        return null;
    }

    @Override
    public OrderDTO findOneById(UUID id) {
        return null;
    }

    @Override
    public OrderDTO findOneByIdOrThrow(UUID id) {
        return null;
    }

    @Override
    public PaginationData<OrderDTO> search(SearchOrderRequestDTO requestDTO, boolean isCount) {
        return null;
    }

}
