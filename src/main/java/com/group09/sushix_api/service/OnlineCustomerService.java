package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.DeliveryOrderCreationRequest;
import com.group09.sushix_api.dto.request.DineInOrderCreationRequest;
import com.group09.sushix_api.dto.request.ReservationRequest;
import com.group09.sushix_api.dto.response.DeliveryOrderResponse;
import com.group09.sushix_api.dto.response.DineInOrderResponse;
import com.group09.sushix_api.dto.response.OrderResponse;
import com.group09.sushix_api.dto.response.ReservationResponse;
import com.group09.sushix_api.entity.Account;
import com.group09.sushix_api.entity.Customer;
import com.group09.sushix_api.entity.Order;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.CustomerMapper;
import com.group09.sushix_api.mapper.OrderMapper;
import com.group09.sushix_api.mapper.ReservationMapper;
import com.group09.sushix_api.repository.*;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OnlineCustomerService {
    ReservationRepository reservationRepository;
    OrderRepository orderRepository;
    AccountRepository accountRepository;
    CustomerRepository customerRepository;

    ReservationMapper reservationMapper;
    OrderMapper orderMapper;

    @Transactional(rollbackFor = Exception.class)
    public ReservationResponse createReservation(ReservationRequest request) {
        Customer customer = new Customer();
        customer.setCustName(request.getCustName());
        customer.setCustEmail(request.getCustEmail());
        customer.setCustPhoneNumber(request.getCustPhoneNumber());
        customer = customerRepository.save(customer);

        Integer rsId = reservationRepository.insertReservation(
                request.getNumOfGuests(),
                request.getArrivalDateTime(),
                request.getRsNotes(),
                request.getBranchId(),
                customer.getCustId()
        );

        Integer orderId = orderRepository.insertDineInOrder(
                customer.getCustId(),
                request.getBranchId(),
                null,
                null,
                rsId
        );

        for (var orderDetailRequest : request.getOrderDetails()) {
            orderRepository.insertOrderDetails(
                    orderId,
                    orderDetailRequest.getItemId(),
                    orderDetailRequest.getQuantity()
            );
        }

        return reservationMapper.toReservationResponse(
                reservationRepository
                        .findById(rsId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED))
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public DeliveryOrderResponse createDeliveryOrder(DeliveryOrderCreationRequest request) {
        Customer customer = null;

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.equals(auth.getName(), "anonymousUser")) {
            Integer accountId = Integer.valueOf(auth.getName());
            Account account = accountRepository
                    .findById(accountId)
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));
            customer = account.getCustomer();
        }
        else {
            customer = new Customer();
            customer.setCustEmail(request.getCustEmail());
            customer.setCustName(request.getCustName());
            customer.setCustPhoneNumber(request.getCustPhoneNumber());
            customer = customerRepository.save(customer);
        }

        Integer orderId = orderRepository.insertDeliveryOrder(
                customer.getCustId(),
                request.getBranchId(),
                request.getDeliveryAddress(),
                request.getDeliveryDateTime()
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
        return DeliveryOrderResponse.builder()
                .order(orderMapper.toOrderResponse(order))
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryDateTime(request.getDeliveryDateTime())
                .orderDetails(request.getOrderDetails())
                .build();
    }
}
