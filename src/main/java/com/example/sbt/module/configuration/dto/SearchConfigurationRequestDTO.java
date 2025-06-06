package com.example.sbt.module.configuration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchConfigurationRequestDTO {

    private Long pageNumber;
    private Long pageSize;
    private String code;
    private String status;
    private Instant createdAtFrom;
    private Instant createdAtTo;

}
