package com.example.sbt.module.cart.service;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.SQLHelper;
import com.example.sbt.module.cart.dto.CartDTO;
import com.example.sbt.module.cart.dto.SearchCartRequestDTO;
import com.example.sbt.module.cart.entity.Cart;
import com.example.sbt.module.cart.repository.CartRepository;
import com.example.sbt.module.cart.repository.CartItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class CartServiceImpl implements CartService {

    private final CommonMapper commonMapper;
    private final EntityManager entityManager;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartDTO init(UUID userId) {
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

    @Override
    public void deleteById(UUID cartId, UUID userId) {
        if (cartId == null || userId == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        findOneByIdOrThrow(cartId, userId);
        cartItemRepository.deleteAllByCartId(cartId);
        cartRepository.deleteByIdAndUserId(cartId, userId);
    }

    @Override
    public CartDTO findOneLatestByUserId(UUID userId) {
        Optional<Cart> result = cartRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
        return result.map(commonMapper::toDTO).orElse(null);
    }

    @Override
    public CartDTO findOneOrInitByUserId(UUID userId) {
        Optional<Cart> result = cartRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
        if (result.isEmpty()) {
            return init(userId);
        }
        return commonMapper.toDTO(result.get());
    }

    @Override
    public CartDTO findOneById(UUID cartId, UUID userId) {
        Optional<Cart> result = cartRepository.findTopByIdAndUserId(cartId, userId);
        return result.map(commonMapper::toDTO).orElse(null);
    }

    @Override
    public CartDTO findOneByIdOrThrow(UUID cartId, UUID userId) {
        CartDTO result = findOneById(cartId, userId);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public PaginationData<CartDTO> search(SearchCartRequestDTO requestDTO, boolean isCount) {
        PaginationData<CartDTO> result = executeSearch(requestDTO, true);
        if (!isCount && result.getTotalItems() > 0) {
            result.setItems(executeSearch(requestDTO, false).getItems());
        }
        return result;
    }

    private PaginationData<CartDTO> executeSearch(SearchCartRequestDTO requestDTO, boolean isCount) {
        PaginationData<CartDTO> result = SQLHelper.initData(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (isCount) {
            builder.append(" select count(*) ");
        } else {
            builder.append(" select c.id, c.user_id, c.created_at, c.updated_at ");
        }
        builder.append(" from cart c ");
        builder.append(" where 1=1 ");
        if (requestDTO.getUserId() != null) {
            builder.append(" and c.user_id >= :userId ");
            params.put("userId", requestDTO.getUserId());
        }
        if (requestDTO.getCreatedAtFrom() != null) {
            builder.append(" and c.created_at >= :createdAtFrom ");
            params.put("createdAtFrom", requestDTO.getCreatedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (requestDTO.getCreatedAtTo() != null) {
            builder.append(" and c.created_at <= :createdAtTo ");
            params.put("createdAtTo", requestDTO.getCreatedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (!isCount) {
            List<String> validOrderBys = List.of("created_at");
            List<String> validOrderDirections = List.of("asc", "desc");
            if (StringUtils.isNotBlank(requestDTO.getOrderBy()) && validOrderBys.contains(requestDTO.getOrderBy())) {
                builder.append(" order by c.").append(requestDTO.getOrderBy()).append(" ");
                if (validOrderDirections.contains(requestDTO.getOrderDirection())) {
                    builder.append(" ").append(requestDTO.getOrderDirection()).append(" ");
                }
                builder.append(" , c.id asc ");
            } else {
                builder.append(" order by c.id desc ");
            }
            builder.append(SQLHelper.toLimitOffset(result.getPageNumber(), result.getPageSize()));
        }
        if (isCount) {
            Query query = entityManager.createNativeQuery(builder.toString());
            SQLHelper.setParams(query, params);
            long count = ConversionUtils.safeToLong(query.getSingleResult());
            result.setTotalItems(count);
            result.setTotalPages(SQLHelper.toPages(count, result.getPageSize()));
        } else {
            Query query = entityManager.createNativeQuery(builder.toString(), ResultSetName.CART_SEARCH);
            SQLHelper.setParams(query, params);
            List<CartDTO> items = query.getResultList();
            result.setItems(items);
        }
        return result;
    }

}
