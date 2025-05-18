package com.example.sbt.module.cart;

import com.example.sbt.common.dto.CommonResponse;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.dto.RequestContextHolder;
import com.example.sbt.common.util.ExceptionUtils;
import com.example.sbt.module.cart.dto.CartDTO;
import com.example.sbt.module.cart.dto.SearchCartRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping("/init")
    public ResponseEntity<CommonResponse<CartDTO>> init() {
        try {
            var result = cartService.init(RequestContextHolder.get().getUserId());
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<CartDTO>> delete(@PathVariable UUID id) {
        try {
            cartService.deleteById(id, RequestContextHolder.get().getUserId());
            return ResponseEntity.ok(new CommonResponse<>());
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<CartDTO>> findOne(@PathVariable UUID id) {
        try {
            var result = cartService.findOneByIdOrThrow(id, RequestContextHolder.get().getUserId());
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<PaginationData<CartDTO>>> search(
            @RequestParam(required = false) Long pageNumber,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) Instant createdAtFrom,
            @RequestParam(required = false) Instant createdAtTo,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDirection,
            @RequestParam(required = false, defaultValue = "false") Boolean count) {
        try {
            var requestDTO = SearchCartRequestDTO.builder()
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .createdAtTo(createdAtTo)
                    .createdAtFrom(createdAtFrom)
                    .orderBy(orderBy)
                    .orderDirection(orderDirection)
                    .userId(RequestContextHolder.get().getUserId())
                    .build();
            var result = cartService.search(requestDTO, count);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

}
