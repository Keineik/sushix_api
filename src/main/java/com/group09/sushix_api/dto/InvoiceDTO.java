package com.group09.sushix_api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public interface InvoiceDTO {
    Integer getInvoiceId();
    Integer getOrderId();
    Integer getBranchId();
    String getInvoiceDate();
    String getPaymentMethod();
    BigDecimal getShippingCost();
    BigDecimal getSubtotal();
    BigDecimal getTaxRate();
    BigDecimal getDiscountRate();
    Integer getCouponId();
    BigDecimal getTotal();
    Integer getCustID();
    String getCustName();
    String getCustPhoneNumber();
    String getCustEmail();
}
