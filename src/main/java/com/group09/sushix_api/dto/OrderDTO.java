package com.group09.sushix_api.dto;

import java.math.BigDecimal;

public interface OrderDTO {
    Integer getOrderId();
    String getOrderDateTime();
    String getOrderStatus();
    Integer getBranchId();
    Integer getCustId();
    String getCustName();
    String getCustPhoneNumber();
    String getCustEmail();
    String getOrderType();
    BigDecimal getEstimatedPrice();
}
