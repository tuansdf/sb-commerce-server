package com.example.sbt.module.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchPaymentRequestDTO {

    private Long pageNumber;
    private Long pageSize;
    private Instant createdAtFrom;
    private Instant createdAtTo;
    private String orderBy;
    private String orderDirection;

}
