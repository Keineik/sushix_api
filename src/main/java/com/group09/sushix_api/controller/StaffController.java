package com.group09.sushix_api.controller;


import com.group09.sushix_api.dto.request.StaffRequest;
import com.group09.sushix_api.dto.response.AccountResponse;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.StaffResponse;
import com.group09.sushix_api.dto.response.WorkHistoryResponse;
import com.group09.sushix_api.service.StaffService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StaffController {
    StaffService staffService;


    @GetMapping("/{staffId}")
    public ApiResponse<StaffResponse> getStaff(@PathVariable("staffId") Integer staffId) {
        return ApiResponse.<StaffResponse>builder()
                .result(staffService.getStaff(staffId))
                .build();
    }

    @GetMapping("/{staffId}/work-history")
    public ApiResponse<List<WorkHistoryResponse>> getStaffWorkHistory(@PathVariable("staffId") Integer staffId) {
        return ApiResponse.<List<WorkHistoryResponse>>builder()
                .result(staffService.getStaffWorkHistory(staffId))
                .build();
    }

    @PostMapping
    public ApiResponse<StaffResponse> createStaff(@RequestBody @Valid StaffRequest request) {
        return ApiResponse.<StaffResponse>builder()
                .result(staffService.createStaff(request))
                .build();
    }

    @PutMapping("/{staffId}")
    public ApiResponse<StaffResponse> updateStaff(@PathVariable("staffId") Integer staffId,
                                                  @RequestBody @Valid StaffRequest request) {
        return ApiResponse.<StaffResponse>builder()
                .result(staffService.updateStaff(staffId, request))
                .build();
    }

    @DeleteMapping("/{staffId}")
    public ApiResponse<String> deleteStaff(@PathVariable("staffId") Integer staffId) {
        staffService.deleteStaff(staffId);
        return ApiResponse.<String>builder()
                .result("Staff member has been deleted")
                .build();
    }
}
