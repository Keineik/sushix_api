package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardTypeResponse {
    Integer cardTypeId;
    String cardName;
    BigDecimal discountRate;
    Integer pointsRequiredForRenewal;
    Integer pointsRequiredForUpgrade;
}
