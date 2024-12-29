package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "[Order]")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    Integer orderId;

    @Column(name = "OrderDateTime")
    Instant orderDateTime;

    @Column(name = "OrderStatus", length = 50)
    String orderStatus;

    @ManyToOne
    @JoinColumn(name = "StaffID")
    Staff staff;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CustID", nullable = false)
    Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "BranchID", nullable = false)
    Branch branch;

    @OneToMany(mappedBy = "order")
    Set<OrderDetails> orderDetails;
}