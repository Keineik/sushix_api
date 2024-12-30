package com.group09.sushix_api.dto.response;

import com.group09.sushix_api.dto.request.OrderDetailsRequest;
import com.group09.sushix_api.entity.Order;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryOrderResponse {
    OrderResponse order;
    String deliveryAddress;
    String deliveryDateTime;
    Set<OrderDetailsRequest> orderDetails;
}
