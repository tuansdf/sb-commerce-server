package com.example.sbt.module.order.entity;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.entity.BaseEntity;
import com.example.sbt.module.order.dto.OrderDTO;
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
        name = "_order",
        indexes = {
                @Index(name = "order_user_id_idx", columnList = "user_id"),
                @Index(name = "order_created_at_idx", columnList = "created_at"),
        }
)
@SqlResultSetMapping(name = ResultSetName.ORDER_SEARCH, classes = {
        @ConstructorResult(targetClass = OrderDTO.class, columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "user_id", type = UUID.class),
                @ColumnResult(name = "total_price", type = Long.class),
                @ColumnResult(name = "status", type = String.class),
                @ColumnResult(name = "created_at", type = Instant.class),
                @ColumnResult(name = "updated_at", type = Instant.class),
        })
})
public class Order extends BaseEntity {

    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "total_price")
    private Long totalPrice;
    @Column(name = "status")
    private String status;

}
