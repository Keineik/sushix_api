package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.ReservationRequest;
import com.group09.sushix_api.dto.response.ReservationResponse;
import com.group09.sushix_api.entity.Reservation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(target = "branchId", source = "branch.branchId")
    @Mapping(target = "custId", source = "customer.custId")
    ReservationResponse toReservationResponse(Reservation reservation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReservation(@MappingTarget Reservation reservation, ReservationRequest request);
}
