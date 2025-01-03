package com.group09.sushix_api.service;


import com.group09.sushix_api.dto.ReservationsDTO;
import com.group09.sushix_api.dto.request.ReservationRequest;
import com.group09.sushix_api.dto.response.ReservationResponse;
import com.group09.sushix_api.entity.Reservation;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.ReservationMapper;
import com.group09.sushix_api.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReservationService {
    ReservationRepository reservationRepository;
    ReservationMapper reservationMapper;

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> fetchReservations(
            Integer page,
            Integer limit,
            String searchTerm,
            String status,
            Integer custId,
            Integer branchId,
            Boolean sortDirection
    ) {
        List<ReservationsDTO> items = reservationRepository.fetchReservations(
                page, limit, searchTerm, status, custId, branchId, sortDirection);
        Integer totalCount = reservationRepository.fetchReservationsCount(searchTerm, status, branchId);

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("totalCount", totalCount);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public ReservationResponse getReservation(Integer rsId) {
        return reservationMapper.toReservationResponse(
                reservationRepository
                        .findById(rsId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED))
        );
    }

    public ReservationResponse updateReservation(Integer rsId, ReservationRequest request) {
        Reservation reservation = reservationRepository
                .findById(rsId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        reservationMapper.updateReservation(reservation, request);

        return reservationMapper.toReservationResponse(reservationRepository.save(reservation));
    }

    public void deleteReservation(Integer rsId) {
        reservationRepository.deleteById(rsId);
    }
}
