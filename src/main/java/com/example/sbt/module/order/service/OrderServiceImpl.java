package com.example.sbt.module.order.service;

import com.example.sbt.common.constant.CommonStatus;
import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.LocaleHelper;
import com.example.sbt.common.util.LocaleHelper.LocaleKey;
import com.example.sbt.common.util.SQLHelper;
import com.example.sbt.module.cart.dto.CartDTO;
import com.example.sbt.module.cart.dto.CartItemDTO;
import com.example.sbt.module.cart.service.CartService;
import com.example.sbt.module.order.dto.OrderDTO;
import com.example.sbt.module.order.dto.OrderItemDTO;
import com.example.sbt.module.order.dto.SearchOrderRequestDTO;
import com.example.sbt.module.order.entity.Order;
import com.example.sbt.module.order.entity.OrderItem;
import com.example.sbt.module.order.repository.OrderItemRepository;
import com.example.sbt.module.order.repository.OrderRepository;
import com.example.sbt.module.product.ProductService;
import com.example.sbt.module.product.dto.ProductDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class OrderServiceImpl implements OrderService {

    private final EntityManager entityManager;
    private final CommonMapper commonMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ProductService productService;

    private List<OrderItemDTO> toOrderItems(List<CartItemDTO> cartItemDTO) {
        if (CollectionUtils.isEmpty(cartItemDTO)) {
            throw new CustomException(LocaleHelper.getMessage("form.error.empty", new LocaleKey("field.cart")));
        }
        List<UUID> productIds = cartItemDTO.stream().map(CartItemDTO::getProductId).toList();
        List<ProductDTO> products = productService.findAllByIdIn(productIds);
        if (CollectionUtils.isEmpty(products)) {
            throw new CustomException(LocaleHelper.getMessage("form.error.not_found", new LocaleKey("field.product")));
        }
        List<OrderItemDTO> result = new ArrayList<>();
        for (CartItemDTO item : cartItemDTO) {
            if (item.getProductId() == null) {
                throw new CustomException(LocaleHelper.getMessage("form.error.not_found", new LocaleKey("field.product")));
            }
            Optional<ProductDTO> productDTO = products.stream().filter(product -> product.getId().equals(item.getProductId())).findFirst();
            if (productDTO.isEmpty()) {
                throw new CustomException(LocaleHelper.getMessage("form.error.not_found", new LocaleKey("field.product")));
            }
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setProductId(item.getProductId());
            orderItemDTO.setQuantity(item.getQuantity());
            orderItemDTO.setUnitPrice(productDTO.get().getPrice());
            result.add(orderItemDTO);
        }
        return result;
    }

    private List<OrderItemDTO> findAllItemsByOrderId(UUID orderId) {
        if (orderId == null) return null;
        List<OrderItem> items = orderItemRepository.findAllByOrderId(orderId);
        if (CollectionUtils.isEmpty(items)) return null;
        return items.stream().map(commonMapper::toDTO).toList();
    }

    @Override
    public OrderDTO createByUserId(UUID userId) {
        if (userId == null) {
            throw new CustomException(LocaleHelper.getMessage("form.error.not_found", new LocaleKey("field.user")));
        }
        CartDTO cartDTO = cartService.findOneOrInitByUserId(userId);
        if (cartDTO == null) {
            throw new CustomException(LocaleHelper.getMessage("form.error.empty", new LocaleKey("field.cart")));
        }
        if (CollectionUtils.isEmpty(cartDTO.getItems())) {
            throw new CustomException(LocaleHelper.getMessage("form.error.empty", new LocaleKey("field.cart")));
        }
        List<OrderItemDTO> orderItemDTOs = toOrderItems(cartDTO.getItems());
        long totalPrice = orderItemDTOs.stream()
                .mapToLong(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
        Order order = new Order();
        order.setStatus(CommonStatus.PAYMENT_PENDING);
        order.setTotalPrice(totalPrice);
        order.setUserId(userId);
        order = orderRepository.save(order);
        OrderDTO result = commonMapper.toDTO(order);
        UUID orderId = result.getId();
        List<OrderItem> orderItems = orderItemDTOs.stream().map(item -> {
            OrderItem orderItem = commonMapper.toEntity(item);
            orderItem.setOrderId(orderId);
            return orderItem;
        }).toList();
        orderItems = orderItemRepository.saveAll(orderItems);
        result.setItems(orderItems.stream().map(commonMapper::toDTO).toList());
        cartService.deleteAllItemsByUserId(userId);
        return result;
    }

    @Override
    public OrderDTO findOneById(UUID id, UUID userId) {
        if (id == null || userId == null) return null;
        Optional<Order> orderOptional = orderRepository.findTopByIdAndUserId(id, userId);
        if (orderOptional.isEmpty()) return null;
        OrderDTO result = commonMapper.toDTO(orderOptional.get());
        result.setItems(findAllItemsByOrderId(result.getId()));
        return result;
    }

    @Override
    public OrderDTO findOneByIdOrThrow(UUID id, UUID userId) {
        OrderDTO result = findOneById(id, userId);
        if (result == null) {
            throw new CustomException(LocaleHelper.getMessage("form.error.not_found", new LocaleKey("field.order")));
        }
        return result;
    }

    @Override
    public PaginationData<OrderDTO> search(SearchOrderRequestDTO requestDTO, boolean isCount) {
        PaginationData<OrderDTO> result = executeSearch(requestDTO, true);
        if (!isCount && result.getTotalItems() > 0) {
            result.setItems(executeSearch(requestDTO, false).getItems());
        }
        return result;
    }

    private PaginationData<OrderDTO> executeSearch(SearchOrderRequestDTO requestDTO, boolean isCount) {
        PaginationData<OrderDTO> result = SQLHelper.initData(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (isCount) {
            builder.append(" select count(*) ");
        } else {
            builder.append(" select o.* ");
        }
        builder.append(" from _order o ");
        builder.append(" where 1=1 ");
        if (requestDTO.getUserId() != null) {
            builder.append(" and o.user_id = :userId ");
            params.put("userId", requestDTO.getUserId());
        }
        if (requestDTO.getCreatedAtFrom() != null) {
            builder.append(" and o.created_at >= :createdAtFrom ");
            params.put("createdAtFrom", requestDTO.getCreatedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (requestDTO.getCreatedAtTo() != null) {
            builder.append(" and o.created_at <= :createdAtTo ");
            params.put("createdAtTo", requestDTO.getCreatedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (!isCount) {
            List<String> validOrderBys = List.of("created_at");
            List<String> validOrderDirections = List.of("asc", "desc");
            if (StringUtils.isNotBlank(requestDTO.getOrderBy()) && validOrderBys.contains(requestDTO.getOrderBy())) {
                builder.append(" order by o.").append(requestDTO.getOrderBy()).append(" ");
                if (validOrderDirections.contains(requestDTO.getOrderDirection())) {
                    builder.append(" ").append(requestDTO.getOrderDirection()).append(" ");
                }
                builder.append(" , o.id asc ");
            } else {
                builder.append(" order by o.id desc ");
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
            Query query = entityManager.createNativeQuery(builder.toString(), ResultSetName.ORDER_SEARCH);
            SQLHelper.setParams(query, params);
            List<OrderDTO> items = query.getResultList();
            setItemsToOrders(items);
            result.setItems(items);
        }
        return result;
    }

    private void setItemsToOrders(List<OrderDTO> orders) {
        final int BATCH = 1000;
        if (CollectionUtils.isEmpty(orders)) return;
        List<UUID> orderIds = orders.stream().map(OrderDTO::getId).toList();
        int totalOrders = orderIds.size();
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < totalOrders; i += BATCH) {
            int end = Math.min(i + BATCH, totalOrders);
            List<OrderItem> items = orderItemRepository.findAllByOrderIdIn(orderIds.subList(i, end));
            if (CollectionUtils.isNotEmpty(items)) {
                orderItems.addAll(items);
            }
        }
        if (CollectionUtils.isEmpty(orderItems)) return;
        Map<UUID, List<OrderItemDTO>> orderItemsMap = orderItems.stream()
                .map(commonMapper::toDTO)
                .collect(Collectors.groupingBy(OrderItemDTO::getOrderId, Collectors.toList()));
        for (OrderDTO order : orders) {
            order.setItems(orderItemsMap.get(order.getId()));
        }
    }

}
