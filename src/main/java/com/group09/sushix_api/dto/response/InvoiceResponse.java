package com.group09.sushix_api.dto.response;

import com.group09.sushix_api.dto.request.OrderDetailsRequest;
import com.group09.sushix_api.entity.Branch;
import com.group09.sushix_api.entity.Customer;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceResponse {
    Integer invoiceId;
    Integer orderId;
    BigDecimal subtotal;
    Float taxRate;
    BigDecimal shippingCost;
    String paymentMethod;
    String invoiceDate;
    CouponResponse coupon;
    BranchResponse branch;
    CustomerResponse customer;
    List<OrderDetailsResponse> orderDetails;
}
