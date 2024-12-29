package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.DineInOrderCreationRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.DineInOrderResponse;
import com.group09.sushix_api.service.StaffWorkService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StaffWorkController {
    StaffWorkService staffWorkService;

    @PostMapping("dine-in-order")
    ApiResponse<DineInOrderResponse> createDineInOrder(@RequestBody DineInOrderCreationRequest request) {
        return ApiResponse.<DineInOrderResponse>builder()
                .result(staffWorkService.createDineInOrder(request))
                .build();
    }
}
