package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.ReservationRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.ReservationResponse;
import com.group09.sushix_api.service.ReservationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") Integer branchId,
            @RequestParam(defaultValue = "false") Boolean sortDirection
    ) {
        Map<String, Object> result = reservationService.fetchReservations(
                page,
                limit,
                searchTerm,
                status,
                branchId,
                sortDirection
        );

        return ApiResponse.<Map<String, Object>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{reservationId}")
    ApiResponse<ReservationResponse> getReservation(@PathVariable("reservationId") Integer rsId) {
        return ApiResponse.<ReservationResponse>builder()
                .result(reservationService.getReservation(rsId))
                .build();
    }

    @PutMapping("/{reservationId}")
    ApiResponse<ReservationResponse> updateReservation(@PathVariable("reservationId") Integer rsId,
                                                       @RequestBody ReservationRequest request) {
        return ApiResponse.<ReservationResponse>builder()
                .result(reservationService.updateReservation(rsId, request))
                .build();
    }

    @DeleteMapping("/{reservationId}")
    ApiResponse<String> deleteReservation(@PathVariable("reservationId") Integer rsId) {
        reservationService.deleteReservation(rsId);
        return ApiResponse.<String>builder()
                .result("Successfully deleted reservation")
                .build();
    }
}
