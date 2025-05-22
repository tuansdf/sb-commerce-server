package com.example.sbt.module.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {

    private UUID id;
    private UUID userId;
    private Long totalPrice;
    private List<OrderItemDTO> items;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    public OrderDTO(UUID id, UUID userId, Long totalPrice, String status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
