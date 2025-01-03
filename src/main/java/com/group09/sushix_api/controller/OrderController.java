package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.DeliveryOrderResponse;
import com.group09.sushix_api.dto.response.DineInOrderResponse;
import com.group09.sushix_api.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderController {
    OrderService orderService;

    @GetMapping
    ApiResponse<Map<String, Object>> fetchOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "18") Integer limit,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") Integer branchId,
            @RequestParam(defaultValue = "0") Integer custId,
            @RequestParam(defaultValue = "") String orderStatus,
            @RequestParam(defaultValue = "") String orderType,
            @RequestParam(defaultValue = "OrderDateTime") String sortKey,
            @RequestParam(defaultValue = "1") Boolean sortDirection
    ) {
        Map<String, Object> result = orderService.fetchOrders(
                page, limit, searchTerm, branchId, custId, orderStatus, orderType, sortKey, sortDirection
        );
        return ApiResponse.<Map<String, Object>>builder()
                .result(result)
                .build();
    }

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

    @GetMapping("{orderId}")
    ApiResponse<Object> getOrder(@PathVariable("orderId") Integer orderId) {
        return ApiResponse.<Object>builder()
                .result(orderService.getOrder(orderId))
                .build();
    }
}
