package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.response.OrderDetailsResponse;
import com.group09.sushix_api.entity.OrderDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailsMapper {
    @Mapping(target = "itemId", source = "item.itemId")
    OrderDetailsResponse toOrderDetailsResponse(OrderDetails orderDetails);
}
