package com.example.sbt.module.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemDTO {

    private UUID id;
    private UUID orderId;
    private UUID productId;
    private Integer quantity;
    private Long unitPrice;
    private Instant createdAt;
    private Instant updatedAt;

}
