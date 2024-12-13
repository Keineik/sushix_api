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

    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "1.0", inclusive = true)
    BigDecimal discountRate;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal minPurchase;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal maxDiscount;

    @NotNull
    LocalDate effectiveDate;

    @NotNull
    LocalDate expiryDate;

    @NotNull
    @Min(1)
    Integer totalUsageLimit;

    Integer minMembershipRequirement;
}
