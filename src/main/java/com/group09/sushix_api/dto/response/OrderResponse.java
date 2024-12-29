package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Integer orderId;
    String orderDateTime;
    String orderStatus;
    Integer staffId;
    Integer custId;
    Integer branchId;
    String orderType;
}
