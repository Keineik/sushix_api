package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.DeliveryOrderResponse;
import com.group09.sushix_api.dto.response.DineInOrderResponse;
import com.group09.sushix_api.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderController {
    OrderService orderService;

    @GetMapping("/dine-in/{orderId}")
    ApiResponse<DineInOrderResponse> getDineInOrder(@PathVariable("orderId") Integer orderId) {
        return ApiResponse.<DineInOrderResponse>builder()
                .result(orderService.getDineInOrder(orderId))
                .build();
    }

    @GetMapping("/delivery/{orderId}")
    ApiResponse<DeliveryOrderResponse> getDeliveryOrder(@PathVariable("orderId") Integer orderId) {
        return ApiResponse.<DeliveryOrderResponse>builder()
                .result(orderService.getDeliveryOrder(orderId))
                .build();
    }
}
