package com.example.sbt.module.order;

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
        name = "order",
        indexes = {
                @Index(name = "order_user_id_idx", columnList = "user_id"),
                @Index(name = "order_created_at_idx", columnList = "created_at"),
        }
)
public class Order extends BaseEntity {

    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "status")
    private String status;
    @Column(name = "total_price")
    private String totalPrice;

}
