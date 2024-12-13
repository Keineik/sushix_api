package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.CouponRequest;
import com.group09.sushix_api.dto.response.CouponResponse;
import com.group09.sushix_api.entity.Coupon;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.CouponMapper;
import com.group09.sushix_api.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CouponService {
    CouponRepository couponRepository;
    CouponMapper couponMapper;

    public List<CouponResponse> getAllCoupons() {
        return couponRepository
                .findAll()
                .stream()
                .map(couponMapper::toCouponResponse)
                .toList();
    }

    public CouponResponse getCoupon(Integer couponId) {
        return couponMapper.toCouponResponse(
                couponRepository
                        .findById(couponId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));
    }

    public CouponResponse createCoupon(CouponRequest request) {
        Coupon coupon = couponMapper.toCoupon(request);

        return couponMapper.toCouponResponse(couponRepository.save(coupon));
    }

    public CouponResponse updateCoupon(Integer couponId, CouponRequest request) {
        Coupon coupon = couponRepository
                .findById(couponId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        couponMapper.updateCoupon(coupon, request);

        return couponMapper.toCouponResponse(couponRepository.save(coupon));
    }

    public void deleteCoupon(Integer couponId) {
        couponRepository.deleteById(couponId);
    }
}
