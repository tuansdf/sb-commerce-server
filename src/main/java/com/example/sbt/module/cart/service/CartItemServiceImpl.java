package com.example.sbt.module.cart.service;

import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.module.cart.dto.CartDTO;
import com.example.sbt.module.cart.dto.CartItemDTO;
import com.example.sbt.module.cart.entity.CartItem;
import com.example.sbt.module.cart.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class CartItemServiceImpl implements CartItemService {

    private final CommonMapper commonMapper;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    private final int MIN_QUANTITY = 1;

    private CartItemDTO validateItemByUser(UUID itemId, UUID userId) {
        CartItemDTO cartItemDTO = findOneByIdOrThrow(itemId);
        cartService.findOneByIdOrThrow(cartItemDTO.getCartId(), userId);
        return cartItemDTO;
    }

    @Override
    public CartItemDTO save(CartItemDTO requestDTO, UUID userId) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (requestDTO.getProductId() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        CartDTO cartDTO = cartService.findOneOrInitByUserId(userId);
        CartItem result = null;
        Optional<CartItem> oldItemOptional = cartItemRepository.findTopByCartIdAndProductId(cartDTO.getId(), requestDTO.getProductId());
        if (oldItemOptional.isPresent()) {
            result = oldItemOptional.get();
        } else {
            requestDTO.setId(null);
            result = commonMapper.toEntity(requestDTO);
        }
        result.setCartId(cartDTO.getId());
        if (ConversionUtils.safeToInteger(requestDTO.getQuantity()) < MIN_QUANTITY) {
            result.setQuantity(MIN_QUANTITY);
        } else {
            result.setQuantity(requestDTO.getQuantity());
        }
        return commonMapper.toDTO(cartItemRepository.save(result));
    }

    @Override
    public CartItemDTO setQuantity(CartItemDTO requestDTO, UUID userId) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (requestDTO.getId() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        CartItemDTO cartItemDTO = validateItemByUser(requestDTO.getId(), userId);
        cartItemDTO.setQuantity(requestDTO.getQuantity());
        return commonMapper.toDTO(cartItemRepository.save(commonMapper.toEntity(cartItemDTO)));
    }

    @Override
    public CartItemDTO findOneById(UUID id) {
        return cartItemRepository.findById(id).map(commonMapper::toDTO).orElse(null);
    }

    @Override
    public CartItemDTO findOneByIdOrThrow(UUID id) {
        CartItemDTO result = findOneById(id);
        if (id == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public void deleteAllByLatestUserCart(UUID userId) {
        CartDTO cartDTO = cartService.findOneLatestByUserId(userId);
        if (cartDTO == null) return;
        cartItemRepository.deleteAllByCartId(cartDTO.getId());
    }

    @Override
    public void deleteAllByCartId(UUID cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
    }

    @Override
    public void deleteAllByCartId(UUID cartId, UUID userId) {
        cartService.findOneByIdOrThrow(cartId, userId);
        cartItemRepository.deleteAllByCartId(cartId);
    }

    @Override
    public void deleteById(UUID id, UUID userId) {
        validateItemByUser(id, userId);
        cartItemRepository.deleteById(id);
    }

}
