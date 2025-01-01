package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "DeliveryOrder")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryOrder {
    @Id
    @Column(name = "OrderID")
    Integer orderId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "OrderID")
    Order order;

    @Column(name = "DeliveryAddress", nullable = false, length = 2047)
    String deliveryAddress;

    @Column(name = "DeliveryDateTime", nullable = false)
    String deliveryDateTime;
}

