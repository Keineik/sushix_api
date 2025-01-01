package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.service.ReservationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReservationController {
    ReservationService reservationService;

    @GetMapping
    ApiResponse<Map<String, Object>> fetchReservations(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "18") Integer limit,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") Integer branchId,
            @RequestParam(defaultValue = "false") Boolean sortDirection
    ) {
        Map<String, Object> result = reservationService.fetchReservations(
                page,
                limit,
                searchTerm,
                branchId,
                sortDirection
        );

        return ApiResponse.<Map<String, Object>>builder()
                .result(result)
                .build();
    }
}
