package com.example.sbt.module.order.entity;

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
        name = "order_item",
        indexes = {
                @Index(name = "order_item_order_id_idx", columnList = "order_id"),
                @Index(name = "order_item_product_id_idx", columnList = "product_id"),
                @Index(name = "order_item_created_at_idx", columnList = "created_at"),
        }
)
public class OrderItem extends BaseEntity {

    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "product_id")
    private UUID productId;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private Long price;

}
