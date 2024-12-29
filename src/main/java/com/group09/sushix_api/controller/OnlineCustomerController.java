package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.DeliveryOrderCreationRequest;
import com.group09.sushix_api.dto.request.ReservationRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.DeliveryOrderResponse;
import com.group09.sushix_api.dto.response.ReservationResponse;
import com.group09.sushix_api.service.OnlineCustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OnlineCustomerController {
    OnlineCustomerService onlineCustomerService;

    @PostMapping("/reservation")
    ApiResponse<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        return ApiResponse.<ReservationResponse>builder()
                .result(onlineCustomerService.createReservation(request))
                .build();
    }

    @PostMapping("/delivery-order")
    ApiResponse<DeliveryOrderResponse> createDineInOrder(@RequestBody DeliveryOrderCreationRequest request) {
        return ApiResponse.<DeliveryOrderResponse>builder()
                .result(onlineCustomerService.createDeliveryOrder(request))
                .build();
    }


}
