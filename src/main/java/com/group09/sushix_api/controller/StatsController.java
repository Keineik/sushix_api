package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.ItemSalesStatsDTO;
import com.group09.sushix_api.dto.RevenueStatsDTO;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StatsController {
    StatsService statsService;

    @GetMapping("/revenue")
    public ApiResponse<List<RevenueStatsDTO>> getRevenueStats(
            @RequestParam(defaultValue = "0") Integer branchId,
            @RequestParam(defaultValue = "day") String groupBy
    ) {
        List<RevenueStatsDTO> result = statsService.getRevenueStats(branchId, groupBy);
        return ApiResponse.<List<RevenueStatsDTO>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/item-sales")
    public ApiResponse<List<ItemSalesStatsDTO>> getItemSalesStats(
            @RequestParam(defaultValue = "0") Integer branchId,
            @RequestParam(defaultValue = "") String region,
            @RequestParam(defaultValue = "0") Integer timePeriod,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "false") Boolean sortDirection,
            @RequestParam(defaultValue = "Revenue") String sortKey
    ) {
        List<ItemSalesStatsDTO> result = statsService.getItemSalesStats(
                branchId,
                region,
                timePeriod,
                limit,
                sortDirection,
                sortKey
        );
        return ApiResponse.<List<ItemSalesStatsDTO>>builder()
                .result(result)
                .build();
    }
}