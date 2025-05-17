package com.example.sbt.module.category;

import com.example.sbt.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(
        name = "category",
        indexes = {
                @Index(name = "category_subcategory_of_idx", columnList = "subcategory_of"),
                @Index(name = "category_created_at_idx", columnList = "created_at"),
        }
)
public class Category extends BaseEntity {

    @Column(name = "subcategory_of")
    private UUID subcategoryOf;
    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;

}
