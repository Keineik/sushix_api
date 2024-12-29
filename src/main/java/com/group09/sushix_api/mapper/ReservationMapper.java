package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.response.ReservationResponse;
import com.group09.sushix_api.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(target = "branchId", source = "branch.branchId")
    @Mapping(target = "custId", source = "customer.custId")
    ReservationResponse toReservationResponse(Reservation reservation);
}
