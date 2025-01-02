package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.response.RestaurantTableResponse;
import com.group09.sushix_api.entity.RestaurantTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantTableMapper {
    @Mapping(target = "branchId", source = "branch.branchId")
    RestaurantTableResponse toRestaurantTableResponse(RestaurantTable restaurantTable);
}
