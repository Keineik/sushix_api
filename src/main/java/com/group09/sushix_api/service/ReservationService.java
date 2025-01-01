package com.group09.sushix_api.service;


import com.group09.sushix_api.dto.ReservationsDTO;
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

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> fetchReservations(
            Integer page,
            Integer limit,
            String searchTerm,
            Integer branchId,
            Boolean sortDirection
    ) {
        List<ReservationsDTO> items = reservationRepository.fetchReservations(page, limit, searchTerm, branchId, sortDirection);
        Integer totalCount = reservationRepository.fetchReservationsCount(searchTerm, branchId);

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("totalCount", totalCount);
        return result;
    }
}
