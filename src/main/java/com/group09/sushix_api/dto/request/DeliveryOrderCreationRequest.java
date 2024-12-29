package com.group09.sushix_api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryOrderCreationRequest {
    String custName;
    String custPhoneNumber;
    String custEmail;

    Integer branchId;
    String deliveryAddress;
    String deliveryDateTime;
    Set<OrderDetailsRequest> orderDetails;
}
