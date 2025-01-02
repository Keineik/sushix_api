package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.ItemSalesStatsDTO;
import com.group09.sushix_api.dto.RevenueStatsDTO;
import com.group09.sushix_api.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class StatsService {
    StatsRepository statsRepository;

    @Transactional(readOnly = true)
    public List<RevenueStatsDTO> getRevenueStats(Integer branchId, String groupBy) {
        return statsRepository.getRevenueStats(branchId, groupBy);
    }

    @Transactional(readOnly = true)
    public List<ItemSalesStatsDTO> getItemSalesStats(
            Integer branchId,
            String region,
            Integer timePeriod,
            Integer limit,
            Boolean sortDirection,
            String sortKey
    ) {
        return statsRepository.getItemSalesStats(branchId,
                region,
                timePeriod,
                limit,
                sortDirection,
                sortKey
        );
    }
}