package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.response.DeliveryOrderResponse;
import com.group09.sushix_api.entity.DeliveryOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryOrderMapper {
    DeliveryOrderResponse toDeliverOrderResponse(DeliveryOrder deliveryOrder);
}
