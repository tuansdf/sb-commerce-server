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
public class SearchOrderRequestDTO {

    private Long pageNumber;
    private Long pageSize;
    private Instant createdAtFrom;
    private Instant createdAtTo;
    private String orderBy;
    private String orderDirection;
    private UUID userId;

}
