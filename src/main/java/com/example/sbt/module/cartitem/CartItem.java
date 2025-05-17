package com.example.sbt.module.cartitem;

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
        name = "cart_item",
        indexes = {
                @Index(name = "cart_item_cart_id_idx", columnList = "cart_id"),
                @Index(name = "cart_item_product_id_idx", columnList = "product_id"),
                @Index(name = "cart_item_created_at_idx", columnList = "created_at"),
        }
)
public class CartItem extends BaseEntity {

    @Column(name = "cart_id")
    private UUID cartId;
    @Column(name = "product_id")
    private UUID productId;
    @Column(name = "quantity")
    private Integer quantity;

}
