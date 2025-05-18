package com.example.sbt.module.payment.dto;

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
public class PaymentDTO {

    private UUID id;
    private UUID orderId;
    private UUID userId;
    private String transactionId;
    private String status;
    private String method;
    private Instant createdAt;
    private Instant updatedAt;

}
