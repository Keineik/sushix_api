package com.group09.sushix_api.dto.response;

import com.group09.sushix_api.dto.request.OrderDetailsRequest;
import com.group09.sushix_api.entity.Order;
import com.group09.sushix_api.entity.RestaurantTable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DineInOrderResponse {
    Order order;
    RestaurantTable table;
    Integer rsId;
    Set<OrderDetailsRequest> menuItems;
}
