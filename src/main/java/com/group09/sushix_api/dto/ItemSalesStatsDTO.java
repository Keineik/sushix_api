package com.group09.sushix_api.dto;

import java.math.BigDecimal;

public interface ItemSalesStatsDTO {
    Integer getItemID();
    String getItemName();
    Integer getBranch();
    Integer getTotalSoldQuantity();
    BigDecimal getTotalRevenue();
}
