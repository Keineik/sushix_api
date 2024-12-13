package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Coupon")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CouponID")
    Integer couponId;

    @Column(name = "CouponCode", nullable = false, unique = true, length = 50)
    String couponCode;

    @Column(name = "CouponDesc", length = 255)
    String couponDesc;

    @Column(name = "DiscountRate", precision = 4, scale = 3)
    BigDecimal discountRate;

    @Column(name = "MinPurchase", nullable = false, precision = 19, scale = 4)
    BigDecimal minPurchase;

    @Column(name = "MaxDiscount", nullable = false, precision = 19, scale = 4)
    BigDecimal maxDiscount;

    @Column(name = "EffectiveDate", nullable = false)
    LocalDate effectiveDate;

    @Column(name = "ExpiryDate", nullable = false)
    LocalDate expiryDate;

    @Column(name = "TotalUsageLimit", nullable = false)
    Integer totalUsageLimit;

    @Column(name = "MinMembershipRequirement")
    Integer minMembershipRequirement;
}
