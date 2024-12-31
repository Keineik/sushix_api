package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.response.OrderDetailsResponse;
import com.group09.sushix_api.entity.OrderDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailsMapper {
    OrderDetailsResponse toOrderDetailsResponse(OrderDetails orderDetails);
}
