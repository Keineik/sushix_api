package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.DineInOrderCreationRequest;
import com.group09.sushix_api.dto.request.InvoiceCreationRequest;
import com.group09.sushix_api.dto.response.DineInOrderResponse;
import com.group09.sushix_api.dto.response.InvoiceResponse;
import com.group09.sushix_api.dto.response.OrderResponse;
import com.group09.sushix_api.dto.response.RestaurantTableResponse;
import com.group09.sushix_api.entity.*;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.OrderDetailsMapper;
import com.group09.sushix_api.mapper.OrderMapper;
import com.group09.sushix_api.mapper.RestaurantTableMapper;
import com.group09.sushix_api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class StaffWorkService {
    OrderRepository orderRepository;
    AccountRepository accountRepository;
    OrderMapper orderMapper;
    InvoiceRepository invoiceRepository;
    OrderDetailsRepository orderDetailsRepository;
    OrderDetailsMapper orderDetailsMapper;
    RestaurantTableRepository restaurantTableRepository;
    RestaurantTableMapper restaurantTableMapper;

    InvoiceService invoiceService;

    @Transactional(rollbackFor = Exception.class)
    public DineInOrderResponse createDineInOrder(DineInOrderCreationRequest request) {
        Staff staff = getStaffFromAuth();

        Integer orderId = orderRepository.insertDineInOrder(
                request.getCustId(),
                staff.getDepartment().getBranch().getBranchId(),
                staff.getStaffId(),
                request.getTableCode(),
                request.getRsId()
        );

        for (var menuItemRequest : request.getOrderDetails()) {
            orderRepository.insertOrderDetails(
                    orderId,
                    menuItemRequest.getItemId(),
                    menuItemRequest.getQuantity()
            );
        }

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        return DineInOrderResponse.builder()
                .order(orderMapper.toOrderResponse(order))
                .tableCode(request.getTableCode())
                .branchId(staff.getDepartment().getBranch().getBranchId())
                .rsId(request.getRsId())
                .orderDetails(orderDetailsRepository
                        .findAllByOrderId(order.getOrderId())
                        .stream()
                        .map(orderDetailsMapper::toOrderDetailsResponse)
                        .toList())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public DineInOrderResponse updateDineInOrder(Integer orderId,
                                                 DineInOrderCreationRequest request) {
        orderRepository.myDeleteById(orderId);
        return createDineInOrder(request);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderResponse updateOrderStatus(Integer orderId, String orderStatus) {
        Staff staff = getStaffFromAuth();
        orderRepository.updateOrderStatus(orderId, staff.getStaffId(), orderStatus);
        return orderMapper.toOrderResponse(orderRepository
                .findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));
    }

    public InvoiceResponse createInvoice(InvoiceCreationRequest request) {
        Integer invoiceId = invoiceRepository.createInvoice(
                request.getOrderId(),
                request.getPaymentMethod(),
                request.getTaxRate(),
                request.getCouponCode()
        );

        return invoiceService.getInvoice(invoiceId);
    }

    public List<RestaurantTableResponse> getAllRestaurantTables() {
        Staff staff = getStaffFromAuth();
        return restaurantTableRepository
                .getRestaurantTableByBranchID(staff.getDepartment().getBranch().getBranchId())
                .stream()
                .map(restaurantTableMapper::toRestaurantTableResponse)
                .toList();
    }

    private Staff getStaffFromAuth() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.equals(auth.getName(), "anonymousUser"))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        Integer accountId = Integer.valueOf(auth.getName());
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        var staff = account.getStaff();
        if (staff == null)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        return staff;
    }
}
