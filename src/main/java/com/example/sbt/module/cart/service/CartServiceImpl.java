package com.example.sbt.module.cart.service;

import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.LocaleHelper;
import com.example.sbt.common.util.LocaleHelper.LocaleKey;
import com.example.sbt.module.cart.dto.CartDTO;
import com.example.sbt.module.cart.dto.CartItemDTO;
import com.example.sbt.module.cart.entity.Cart;
import com.example.sbt.module.cart.entity.CartItem;
import com.example.sbt.module.cart.repository.CartItemRepository;
import com.example.sbt.module.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class CartServiceImpl implements CartService {

    private final CommonMapper commonMapper;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private CartDTO init(UUID userId) {
        if (userId == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (cartRepository.existsByUserId(userId)) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        Cart result = new Cart();
        result.setUserId(userId);
        return commonMapper.toDTO(cartRepository.save(result));
    }

    private List<CartItemDTO> findAllItemsByCartId(UUID cartId) {
        if (cartId == null) return null;
        List<CartItem> items = cartItemRepository.findAllByCartId(cartId);
        if (CollectionUtils.isEmpty(items)) return null;
        return items.stream().map(commonMapper::toDTO).toList();
    }

    @Override
    public CartDTO findOneOrInitByUserId(UUID userId) {
        if (userId == null) return null;
        Optional<Cart> cartOptional = cartRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
        if (cartOptional.isEmpty()) {
            return init(userId);
        }
        CartDTO result = commonMapper.toDTO(cartOptional.get());
        result.setItems(findAllItemsByCartId(result.getId()));
        return result;
    }

    @Override
    public void deleteAllItemsByUserId(UUID userId) {
        if (userId == null) return;
        Optional<Cart> cartOptional = cartRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
        if (cartOptional.isEmpty()) return;
        cartItemRepository.deleteAllByCartId(cartOptional.get().getId());
    }

    @Override
    public void deleteItemByIdAndUserId(UUID itemId, UUID userId) {
        if (itemId == null || userId == null) return;
        CartItem cartItem = cartItemRepository.findTopByIdAndUserId(itemId, userId).orElse(null);
        if (cartItem == null) return;
        cartItemRepository.deleteById(cartItem.getId());
    }

    @Override
    public CartItemDTO setItemQuantity(CartItemDTO requestDTO, UUID userId) {
        if (requestDTO == null || userId == null || requestDTO.getId() == null) {
            throw new CustomException(LocaleHelper.getMessage("form.error.missing", new LocaleKey("field.cart_item")));
        }
        if (requestDTO.getQuantity() == null) {
            throw new CustomException(LocaleHelper.getMessage("form.error.missing", new LocaleKey("field.quantity")));
        }
        Optional<CartItem> cartItemOptional = cartItemRepository.findTopByIdAndUserId(requestDTO.getId(), userId);
        if (cartItemOptional.isEmpty()) {
            throw new CustomException(LocaleHelper.getMessage("form.error.missing", new LocaleKey("field.cart_item")));
        }
        CartItem cartItem = cartItemOptional.get();
        cartItem.setQuantity(requestDTO.getQuantity());
        return commonMapper.toDTO(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDTO addItem(CartItemDTO requestDTO, UUID userId) {
        if (requestDTO == null || userId == null) {
            throw new CustomException(LocaleHelper.getMessage("form.error.missing", new LocaleKey("field.cart_item")));
        }
        if (requestDTO.getProductId() == null) {
            throw new CustomException(LocaleHelper.getMessage("form.error.missing", new LocaleKey("field.product")));
        }
        if (requestDTO.getQuantity() == null) {
            throw new CustomException(LocaleHelper.getMessage("form.error.missing", new LocaleKey("field.quantity")));
        }
        CartDTO cart = findOneOrInitByUserId(userId);
        CartItem cartItem = cartItemRepository.findTopByCartIdAndProductId(cart.getId(), requestDTO.getProductId()).orElse(new CartItem());
        cartItem.setCartId(cart.getId());
        cartItem.setProductId(requestDTO.getProductId());
        cartItem.setQuantity(requestDTO.getQuantity());
        return commonMapper.toDTO(cartItemRepository.save(cartItem));
    }

}
