package com.example.sbt.module.category.dto;

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
public class CategoryDTO {

    private UUID id;
    private UUID subcategoryOf;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

}
