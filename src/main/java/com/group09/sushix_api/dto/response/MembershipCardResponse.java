package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipCardResponse {
    Integer cardId;
    String issuedDate;
    Integer cardTypeId;
    Integer custId;
    Integer staffId;
    Integer points;
}
