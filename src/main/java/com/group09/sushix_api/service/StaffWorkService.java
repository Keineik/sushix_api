package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.DineInOrderCreationRequest;
import com.group09.sushix_api.dto.response.DineInOrderResponse;
import com.group09.sushix_api.entity.Account;
import com.group09.sushix_api.entity.Order;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.OrderMapper;
import com.group09.sushix_api.repository.AccountRepository;
import com.group09.sushix_api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class StaffWorkService {
    OrderRepository orderRepository;
    AccountRepository accountRepository;

    OrderMapper orderMapper;

    @Transactional(rollbackFor = Exception.class)
    public DineInOrderResponse createDineInOrder(DineInOrderCreationRequest request) {
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
                .orderDetails(request.getOrderDetails())
                .build();
    }
}
