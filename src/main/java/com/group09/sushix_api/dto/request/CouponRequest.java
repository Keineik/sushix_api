package com.group09.sushix_api.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponRequest {
    @NotBlank
    @Size(max = 50)
    String couponCode;

    @Size(max = 255)
    String couponDesc;

    @DecimalMin(value = "0.0")
    BigDecimal discountFlat;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "1.0")
    BigDecimal discountRate;

    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal minPurchase;

    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal maxDiscount;

    LocalDate effectiveDate;

    LocalDate expiryDate;

    @Min(1)
    Integer totalUsageLimit;

    Integer RemainingUsage;

    Integer minMembershipRequirement;
}
