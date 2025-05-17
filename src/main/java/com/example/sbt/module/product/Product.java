package com.example.sbt.module.product;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.entity.BaseEntity;
import com.example.sbt.module.product.dto.ProductDTO;
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
        name = "product",
        indexes = {
                @Index(name = "product_variant_of_idx", columnList = "variant_of"),
                @Index(name = "product_category_id_idx", columnList = "category_id"),
                @Index(name = "product_image_file_id_idx", columnList = "image_file_id"),
                @Index(name = "product_created_at_idx", columnList = "created_at"),
        }
)
@SqlResultSetMapping(name = ResultSetName.PRODUCT_SEARCH, classes = {
        @ConstructorResult(targetClass = ProductDTO.class, columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "description", type = String.class),
                @ColumnResult(name = "stock", type = Integer.class),
                @ColumnResult(name = "price", type = Long.class),
                @ColumnResult(name = "variant_of", type = UUID.class),
                @ColumnResult(name = "category_id", type = UUID.class),
                @ColumnResult(name = "image_url", type = String.class),
                @ColumnResult(name = "created_at", type = Instant.class),
                @ColumnResult(name = "updated_at", type = Instant.class),
        })
})
public class Product extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "stock")
    private Integer stock;
    @Column(name = "price")
    private Long price;
    @Column(name = "variant_of")
    private UUID variantOf;
    @Column(name = "category_id")
    private UUID categoryId;
    @Column(name = "image_file_id")
    private UUID imageFileId;

}
