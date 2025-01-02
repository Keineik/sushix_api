package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.BranchMenuItemRequest;
import com.group09.sushix_api.dto.request.DineInOrderCreationRequest;
import com.group09.sushix_api.dto.request.InvoiceCreationRequest;
import com.group09.sushix_api.dto.response.*;
import com.group09.sushix_api.entity.*;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.BranchMenuItemMapper;
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
    BranchMenuItemMapper branchMenuItemMapper;

    InvoiceService invoiceService;
    BranchMenuItemRepository branchMenuItemRepository;

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

    public List<BranchMenuItemResponse> getAllBranchMenuItems() {
        Staff staff = getStaffFromAuth();
        return branchMenuItemRepository
                .getAllBranchMenuItems(staff.getDepartment().getBranch().getBranchId())
                .stream()
                .map(branchMenuItemMapper::toBranchMenuItemResponse)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public BranchMenuItemResponse createBranchMenuItem(BranchMenuItemRequest request) {
        Staff staff = getStaffFromAuth();
        BranchMenuItem branchMenuItem = branchMenuItemMapper.toBranchMenuItem(request);
        branchMenuItem.setBranchId(staff.getDepartment().getBranch().getBranchId());
        return branchMenuItemMapper
                .toBranchMenuItemResponse(branchMenuItemRepository
                        .save(branchMenuItem));
    }

    @Transactional(rollbackFor = Exception.class)
    public BranchMenuItemResponse updateBranchMenuItem(Integer itemId, BranchMenuItemRequest request) {
        Staff staff = getStaffFromAuth();
        BranchMenuItem branchMenuItem = null;
        try {
            branchMenuItem = branchMenuItemRepository.getBranchMenuItem(
                    staff.getDepartment().getBranch().getBranchId(),
                    itemId
            );
        } catch (Exception e) {
            throw new AppException(ErrorCode.OBJECT_NOT_EXISTED);
        }
        branchMenuItem.setIsShippable(request.getIsShippable());

        return branchMenuItemMapper
                .toBranchMenuItemResponse(branchMenuItemRepository
                        .save(branchMenuItem));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBranchMenuItem(Integer itemId) {
        Staff staff = getStaffFromAuth();
        branchMenuItemRepository
                .deleteBranchMenuItem(staff.getDepartment().getBranch().getBranchId(), itemId);
    }

    private Staff getStaffFromAuth() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.equals(auth.getName(), "anonymousUser"))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        Integer accountId = Integer.valueOf(auth.getName());
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var staff = account.getStaff();
        if (staff == null)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        return staff;
    }
}
