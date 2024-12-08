package com.group09.sushix_api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardTypeRequest {
    String cardName;
    BigDecimal discountRate;
    Integer pointsRequiredForRenewal;
    Integer pointsRequiredForUpgrade;
}
