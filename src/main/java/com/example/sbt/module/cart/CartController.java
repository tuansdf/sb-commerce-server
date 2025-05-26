package com.example.sbt.module.cart;

import com.example.sbt.common.dto.CommonResponse;
import com.example.sbt.common.dto.RequestContextHolder;
import com.example.sbt.common.util.ExceptionUtils;
import com.example.sbt.module.cart.dto.CartDTO;
import com.example.sbt.module.cart.dto.CartItemDTO;
import com.example.sbt.module.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<CartDTO>> findOneOrInit() {
        try {
            var result = cartService.findOneOrInitByUserId(RequestContextHolder.get().getUserId());
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @PutMapping("/items")
    public ResponseEntity<CommonResponse<List<CartItemDTO>>> addItems(@RequestBody CartDTO requestDTO) {
        try {
            var result = cartService.addItems(requestDTO.getItems(), RequestContextHolder.get().getUserId());
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @DeleteMapping("/items")
    public ResponseEntity<CommonResponse<Object>> deleteItems() {
        try {
            cartService.deleteAllItemsByUserId(RequestContextHolder.get().getUserId());
            return ResponseEntity.ok(new CommonResponse<>());
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<CommonResponse<Object>> deleteItem(@PathVariable UUID id) {
        try {
            cartService.deleteItemByIdAndUserId(id, RequestContextHolder.get().getUserId());
            return ResponseEntity.ok(new CommonResponse<>());
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

}
