package com.example.sbt.module.cart.dto;

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
public class CartDTO {

    private UUID id;
    private UUID userId;
    private List<CartItemDTO> items;
    private Instant createdAt;
    private Instant updatedAt;

    public CartDTO(UUID id, UUID userId, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
