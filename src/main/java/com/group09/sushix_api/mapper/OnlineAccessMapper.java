package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.OnlineAccessRequest;
import com.group09.sushix_api.dto.response.OnlineAccessResponse;
import com.group09.sushix_api.entity.OnlineAccess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OnlineAccessMapper {
    @Mapping(target = "custId", source = "customer.custId")
    OnlineAccessResponse toOnlineAccessResponse(OnlineAccess onlineAccess);

    @Mapping(target = "customer", ignore = true)
    OnlineAccessResponse toOnlineAccess(OnlineAccessRequest onlineAccessRequest);
}
