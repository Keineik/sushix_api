package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.DineInOrderCreationRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.DineInOrderResponse;
import com.group09.sushix_api.service.StaffWorkService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StaffWorkController {
    StaffWorkService staffWorkService;

    @PostMapping("/dine-in-order")
    ApiResponse<DineInOrderResponse> createDineInOrder(@RequestBody DineInOrderCreationRequest request) {
        return ApiResponse.<DineInOrderResponse>builder()
                .result(staffWorkService.createDineInOrder(request))
                .build();
    }

    @PutMapping("/dine-in-order/{orderId}")
    ApiResponse<DineInOrderResponse> updateDineInOrder(@PathVariable("orderId") Integer orderId,
                                                       @RequestBody DineInOrderCreationRequest request) {
        return ApiResponse.<DineInOrderResponse>builder()
                .result(staffWorkService.updateDineInOrder(orderId, request))
                .build();
    }
}
