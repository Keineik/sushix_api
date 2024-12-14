package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "OrderDetails")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DetailsID")
    Integer detailsId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OrderID", nullable = false)
    Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ItemID", nullable = false)
    MenuItem item;

    @Column(name = "UnitPrice", precision = 19, scale = 4)
    BigDecimal unitPrice;

    @Column(name = "Quantity")
    Integer quantity;
}