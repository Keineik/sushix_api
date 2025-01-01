package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DineInOrderResponse {
    OrderResponse order;
    Integer tableCode;
    Integer branchId;
    Integer rsId;
    List<OrderDetailsResponse> orderDetails;
}
