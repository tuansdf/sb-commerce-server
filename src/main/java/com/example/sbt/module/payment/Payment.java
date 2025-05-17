package com.example.sbt.module.payment;

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
        name = "payment",
        indexes = {
                @Index(name = "payment_order_id_idx", columnList = "order_id"),
                @Index(name = "payment_user_id_idx", columnList = "user_id"),
                @Index(name = "payment_created_at_idx", columnList = "created_at"),
        }
)
public class Payment extends BaseEntity {

    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "status")
    private String status;
    @Column(name = "method")
    private String method;

}
