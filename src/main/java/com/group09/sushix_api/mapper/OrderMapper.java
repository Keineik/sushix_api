package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.response.OrderResponse;
import com.group09.sushix_api.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "staffId", source = "staff.staffId")
    @Mapping(target = "custId", source = "customer.custId")
    @Mapping(target = "branchId", source = "branch.branchId")
    OrderResponse toOrderResponse(Order order);
}
