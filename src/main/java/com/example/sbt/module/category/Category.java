package com.example.sbt.module.category;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.entity.BaseEntity;
import com.example.sbt.module.category.dto.CategoryDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
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
                @Index(name = "category_parent_id_idx", columnList = "parent_id"),
                @Index(name = "category_created_at_idx", columnList = "created_at"),
        }
)
@SqlResultSetMapping(name = ResultSetName.CATEGORY_SEARCH, classes = {
        @ConstructorResult(targetClass = CategoryDTO.class, columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "parent_id", type = UUID.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "description", type = String.class),
                @ColumnResult(name = "created_at", type = Instant.class),
                @ColumnResult(name = "updated_at", type = Instant.class),
        })
})
public class Category extends BaseEntity {

    @Column(name = "parent_id")
    private UUID parentId;
    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;

}
