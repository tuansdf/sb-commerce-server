package com.example.sbt.module.order;

import com.example.sbt.common.constant.PermissionCode;
import com.example.sbt.common.dto.CommonResponse;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.util.ExceptionUtils;
import com.example.sbt.module.order.dto.OrderDTO;
import com.example.sbt.module.order.dto.SearchOrderRequestDTO;
import com.example.sbt.module.order.service.OrderItemService;
import com.example.sbt.module.order.service.OrderService;
import com.example.sbt.module.order.dto.OrderItemDTO;
import com.example.sbt.module.order.dto.SearchOrderItemRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PutMapping
    @Secured({PermissionCode.SYSTEM_ADMIN})
    public ResponseEntity<CommonResponse<OrderDTO>> save(@RequestBody OrderDTO requestDTO) {
        try {
            var result = orderService.save(requestDTO);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<OrderDTO>> findOne(@PathVariable UUID id) {
        try {
            var result = orderService.findOneByIdOrThrow(id);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<PaginationData<OrderDTO>>> search(
            @RequestParam(required = false) Long pageNumber,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) Instant createdAtFrom,
            @RequestParam(required = false) Instant createdAtTo,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDirection,
            @RequestParam(required = false, defaultValue = "false") Boolean count) {
        try {
            var requestDTO = SearchOrderRequestDTO.builder()
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .createdAtTo(createdAtTo)
                    .createdAtFrom(createdAtFrom)
                    .orderBy(orderBy)
                    .orderDirection(orderDirection)
                    .build();
            var result = orderService.search(requestDTO, count);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    @RestController
    @RequestMapping("/v1/order-items")
    public static class OrderItemController {
    
        private final OrderItemService orderItemService;
    
        @PutMapping
        @Secured({PermissionCode.SYSTEM_ADMIN})
        public ResponseEntity<CommonResponse<OrderItemDTO>> save(@RequestBody OrderItemDTO requestDTO) {
            try {
                var result = orderItemService.save(requestDTO);
                return ResponseEntity.ok(new CommonResponse<>(result));
            } catch (Exception e) {
                return ExceptionUtils.toResponseEntity(e);
            }
        }
    
        @GetMapping("/{id}")
        public ResponseEntity<CommonResponse<OrderItemDTO>> findOne(@PathVariable UUID id) {
            try {
                var result = orderItemService.findOneByIdOrThrow(id);
                return ResponseEntity.ok(new CommonResponse<>(result));
            } catch (Exception e) {
                return ExceptionUtils.toResponseEntity(e);
            }
        }
    
        @GetMapping("/search")
        public ResponseEntity<CommonResponse<PaginationData<OrderItemDTO>>> search(
                @RequestParam(required = false) Long pageNumber,
                @RequestParam(required = false) Long pageSize,
                @RequestParam(required = false) Instant createdAtFrom,
                @RequestParam(required = false) Instant createdAtTo,
                @RequestParam(required = false) String orderBy,
                @RequestParam(required = false) String orderDirection,
                @RequestParam(required = false, defaultValue = "false") Boolean count) {
            try {
                var requestDTO = SearchOrderItemRequestDTO.builder()
                        .pageNumber(pageNumber)
                        .pageSize(pageSize)
                        .createdAtTo(createdAtTo)
                        .createdAtFrom(createdAtFrom)
                        .orderBy(orderBy)
                        .orderDirection(orderDirection)
                        .build();
                var result = orderItemService.search(requestDTO, count);
                return ResponseEntity.ok(new CommonResponse<>(result));
            } catch (Exception e) {
                return ExceptionUtils.toResponseEntity(e);
            }
        }
    
    }
}
