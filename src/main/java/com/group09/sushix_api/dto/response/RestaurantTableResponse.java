package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTableResponse {
    Integer tableId;
    Integer tableCode;
    Integer branchId;
    Integer numOfSeats;
    Boolean isVacant;
}
