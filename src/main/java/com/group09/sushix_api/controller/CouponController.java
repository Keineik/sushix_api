package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.CouponRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.CouponResponse;
import com.group09.sushix_api.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CouponController {
    CouponService couponService;

    @GetMapping
    ApiResponse<List<CouponResponse>> getCoupons() {
        return ApiResponse.<List<CouponResponse>>builder()
                .result(couponService.getAllCoupons())
                .build();
    }

    @GetMapping("/{couponId}")
    ApiResponse<CouponResponse> getCoupon(@PathVariable("couponId") Integer couponId) {
        return ApiResponse.<CouponResponse>builder()
                .result(couponService.getCoupon(couponId))
                .build();
    }

    @PostMapping
    ApiResponse<CouponResponse> createCoupon(@RequestBody @Valid CouponRequest request) {
        return ApiResponse.<CouponResponse>builder()
                .result(couponService.createCoupon(request))
                .build();
    }

    @PutMapping("/{couponId}")
    ApiResponse<CouponResponse> updateCoupon(@PathVariable("couponId") Integer couponId,
                                             @RequestBody @Valid CouponRequest request) {
        return ApiResponse.<CouponResponse>builder()
                .result(couponService.updateCoupon(couponId, request))
                .build();
    }

    @DeleteMapping("/{couponId}")
    ApiResponse<String> deleteCoupon(@PathVariable Integer couponId) {
        couponService.deleteCoupon(couponId);
        return ApiResponse.<String>builder().result("Coupon has been deleted").build();
    }
}
