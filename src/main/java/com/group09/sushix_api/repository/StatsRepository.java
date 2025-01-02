package com.group09.sushix_api.repository;

import com.group09.sushix_api.dto.ItemSalesStatsDTO;
import com.group09.sushix_api.dto.RevenueStatsDTO;
import com.group09.sushix_api.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Invoice, Integer> {
    @Procedure(procedureName = "usp_GetRevenueStats")
    List<RevenueStatsDTO> getRevenueStats(
            @Param("BranchId") Integer branchId,
            @Param("GroupBy") String groupBy
    );

    @Procedure(procedureName = "usp_GetItemSalesStats")
    List<ItemSalesStatsDTO> getItemSalesStats(
            @Param("BranchId") Integer branchId,
            @Param("Region") String region,
            @Param("TimePeriod") Integer timePeriod,
            @Param("Limit") Integer limit,
            @Param("SortDirection") Boolean sortDirection,
            @Param("SortKey") String sortKey
    );
}
