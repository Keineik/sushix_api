package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.response.DeliveryOrderResponse;
import com.group09.sushix_api.dto.response.DineInOrderResponse;
import com.group09.sushix_api.entity.DeliveryOrder;
import com.group09.sushix_api.entity.DineInOrder;
import com.group09.sushix_api.entity.Order;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.OrderDetailsMapper;
import com.group09.sushix_api.mapper.OrderMapper;
import com.group09.sushix_api.repository.DeliveryOrderRepository;
import com.group09.sushix_api.repository.DineInOrderRepository;
import com.group09.sushix_api.repository.OrderDetailsRepository;
import com.group09.sushix_api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    DineInOrderRepository dineInOrderRepository;
    DeliveryOrderRepository deliveryOrderRepository;
    OrderRepository orderRepository;
    OrderDetailsRepository orderDetailsRepository;
    OrderMapper orderMapper;
    OrderDetailsMapper orderDetailsMapper;

    @Transactional(rollbackFor = Exception.class)
    public DineInOrderResponse getDineInOrder(Integer orderId) {
        DineInOrder dineInOrder = dineInOrderRepository
                .findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        return DineInOrderResponse.builder()
                .order(orderMapper.toOrderResponse(order))
                .tableCode(dineInOrder.getTable().getTableCode())
                .branchId(order.getBranch().getBranchId())
                .rsId((dineInOrder
                        .getReservation() == null) ? null : dineInOrder.getReservation().getRsId())
                .orderDetails(orderDetailsRepository
                        .findAllByOrderId(order.getOrderId())
                        .stream()
                        .map(orderDetailsMapper::toOrderDetailsResponse)
                        .toList())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public DeliveryOrderResponse getDeliveryOrder(Integer orderId) {
        DeliveryOrder deliveryOrder = deliveryOrderRepository
                .findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        return DeliveryOrderResponse.builder()
                .order(orderMapper.toOrderResponse(order))
                .deliveryAddress(deliveryOrder.getDeliveryAddress())
                .deliveryDateTime(deliveryOrder.getDeliveryDateTime())
                .orderDetails(orderDetailsRepository
                        .findAllByOrderId(order.getOrderId())
                        .stream()
                        .map(orderDetailsMapper::toOrderDetailsResponse)
                        .toList())
                .build();
    }
}
