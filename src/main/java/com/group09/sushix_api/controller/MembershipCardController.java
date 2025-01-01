package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.MembershipCardRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.MembershipCardResponse;
import com.group09.sushix_api.service.MembershipCardService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/membership-card")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MembershipCardController {
    MembershipCardService membershipCardService;

    @GetMapping
    ApiResponse<List<MembershipCardResponse>> getAllMembershipCards() {
        return ApiResponse.<List<MembershipCardResponse>>builder()
                .result(membershipCardService.getAllMembershipCards())
                .build();
    }

    @GetMapping("/{cardId}")
    ApiResponse<MembershipCardResponse> getMembershipCard(@PathVariable("cardId") Integer cardId) {
        return ApiResponse.<MembershipCardResponse>builder()
                .result(membershipCardService.getMembershipCard(cardId))
                .build();
    }

    @GetMapping("/customer/{custId}")
    ApiResponse<MembershipCardResponse> getMembershipCardByCustomerId(@PathVariable Integer custId) {
        return ApiResponse.<MembershipCardResponse>builder()
                .result(membershipCardService.getMembershipCardByCustomerId(custId))
                .build();
    }

    @PostMapping
    ApiResponse<MembershipCardResponse> createMembershipCard(@RequestBody @Valid MembershipCardRequest request) {
        return ApiResponse.<MembershipCardResponse>builder()
                .result(membershipCardService.createMembershipCard(request))
                .build();
    }

    @DeleteMapping("/{cardId}")
    ApiResponse<String> deleteMembershipCard(@PathVariable Integer cardId) {
        membershipCardService.deleteMembershipCard(cardId);
        return ApiResponse.<String>builder().result("Membership card has been deleted").build();
    }
}
