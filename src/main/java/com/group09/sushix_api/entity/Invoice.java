package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "Invoice")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InvoiceID")
    Integer invoiceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderID", nullable = false)
    Order order;

    @Column(name = "Subtotal", precision = 19, scale = 4)
    BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CouponID")
    Coupon coupon;

    @Column(name = "DiscountRate", precision = 4, scale = 3)
    BigDecimal discountRate;

    @Column(name = "TaxRate", precision = 4, scale = 3)
    BigDecimal taxRate;

    @Column(name = "ShippingCost", precision = 19, scale = 4)
    BigDecimal shippingCost;

    @Column(name = "PaymentMethod", nullable = false, length = 50)
    String paymentMethod;

    @Column(name = "InvoiceDate", nullable = false)
    Instant invoiceDate;
}