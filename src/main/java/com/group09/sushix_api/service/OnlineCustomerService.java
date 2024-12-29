package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.ReservationRequest;
import com.group09.sushix_api.dto.response.ReservationResponse;
import com.group09.sushix_api.entity.Account;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.ReservationMapper;
import com.group09.sushix_api.repository.AccountRepository;
import com.group09.sushix_api.repository.OrderRepository;
import com.group09.sushix_api.repository.ReservationRepository;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class OnlineCustomerService {
    ReservationRepository reservationRepository;
    OrderRepository orderRepository;
    AccountRepository accountRepository;

    ReservationMapper reservationMapper;

    public ReservationResponse createReservation(ReservationRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        Integer accountId = Integer.valueOf(auth.getName());
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        if (account.getCustomer() == null)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Integer rsId = reservationRepository.insertReservation(
                request.getNumOfGuests(),
                request.getArrivalDateTime(),
                request.getRsNotes(),
                request.getBranchId(),
                account.getCustomer().getCustId()
        );

        var menuItems = request.getMenuItems();
        Integer orderId = orderRepository.insertDineInOrder(
                account.getCustomer().getCustId(),
                request.getBranchId(),
                accountId,
                null,
                rsId
        );

        for (var menuItemRequest : request.getMenuItems()) {
            orderRepository.insertOrderDetails(
                    orderId,
                    menuItemRequest.getItemId(),
                    menuItemRequest.getQuantity()
            );
        }

        return reservationMapper.toReservationResponse(
                reservationRepository
                        .findById(rsId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED))
        );
    }
}
