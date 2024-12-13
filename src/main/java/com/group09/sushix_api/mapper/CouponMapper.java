package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.CouponRequest;
import com.group09.sushix_api.dto.response.CouponResponse;
import com.group09.sushix_api.entity.Coupon;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    CouponResponse toCouponResponse(Coupon coupon);

    Coupon toCoupon(CouponRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCoupon(@MappingTarget Coupon coupon, CouponRequest request);
}
