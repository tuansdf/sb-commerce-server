package com.example.sbt.module.cart.entity;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.entity.BaseEntity;
import com.example.sbt.module.cart.dto.CartDTO;
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
@Builder
@Table(
        name = "cart",
        indexes = {
                @Index(name = "cart_user_id_idx", columnList = "user_id"),
                @Index(name = "cart_created_at_idx", columnList = "created_at"),
        }
)
public class Cart extends BaseEntity {

    @Column(name = "user_id")
    private UUID userId;

}
