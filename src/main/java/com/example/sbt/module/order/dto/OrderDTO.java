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
public class OrderDTO {

    private UUID id;
    private UUID userId;
    private String status;
    private String totalPrice;
    private Instant createdAt;
    private Instant updatedAt;

}
