package com.group09.sushix_api.dto.response;

import com.group09.sushix_api.dto.request.OrderDetailsRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

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
    Set<OrderDetailsRequest> orderDetails;
}
