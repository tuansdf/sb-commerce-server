package com.example.sbt.module.product.dto;

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
public class ProductDTO {

    private UUID id;
    private String name;
    private String description;
    private Integer stock;
    private Long price;
    private UUID variantOf;
    private UUID categoryId;
    private UUID imageFileId;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;

    public ProductDTO(UUID id, String name, String description, Integer stock, Long price, UUID variantOf, UUID categoryId, String imageUrl, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.variantOf = variantOf;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
