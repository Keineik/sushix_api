package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponResponse {
    Integer couponId;
    String couponCode;
    String couponDesc;
    BigDecimal discountFlat;
    BigDecimal discountRate;
    BigDecimal minPurchase;
    BigDecimal maxDiscount;
    LocalDate effectiveDate;
    LocalDate expiryDate;
    Integer totalUsageLimit;
    Integer remainingUsage;
    Integer minMembershipRequirement;
}
