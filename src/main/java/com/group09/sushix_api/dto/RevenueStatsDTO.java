package com.group09.sushix_api.dto;

import java.math.BigDecimal;

public interface RevenueStatsDTO {
    String getRevenuePeriod();
    Integer getBranch();
    BigDecimal getTotalRevenue();
}
