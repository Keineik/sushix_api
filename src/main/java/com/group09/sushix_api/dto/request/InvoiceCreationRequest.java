package com.group09.sushix_api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceCreationRequest {
    Integer orderId;
    String paymentMethod;
    Float taxRate;
    String couponCode;
}
