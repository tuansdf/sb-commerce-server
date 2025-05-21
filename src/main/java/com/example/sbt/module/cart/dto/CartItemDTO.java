package com.example.sbt.module.cart.dto;

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
public class CartItemDTO {

    private UUID id;
    private UUID cartId;
    private UUID productId;
    private Integer quantity;
    private Instant createdAt;
    private Instant updatedAt;

}
