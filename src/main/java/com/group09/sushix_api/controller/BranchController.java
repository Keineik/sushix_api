package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.BranchRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.BranchResponse;
import com.group09.sushix_api.service.BranchService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/branch")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BranchController {
    BranchService branchService;

    @GetMapping
    ApiResponse<List<BranchResponse>> getBranches() {
        return ApiResponse.<List<BranchResponse>>builder()
                .result(branchService.getAllBranches())
                .build();
    }

    @GetMapping("/{branchId}")
    ApiResponse<BranchResponse> getBranch(@PathVariable("branchId") Integer branchId) {
        return ApiResponse.<BranchResponse>builder()
                .result(branchService.getBranch(branchId))
                .build();
    }

    @PostMapping
    ApiResponse<BranchResponse> createBranch(@RequestBody @Valid BranchRequest request) {
        return ApiResponse.<BranchResponse>builder()
                .result(branchService.createBranch(request))
                .build();
    }

    @PutMapping("/{branchId}")
    ApiResponse<BranchResponse> updateBranch(@PathVariable("branchId") Integer branchId,
                                             @RequestBody @Valid BranchRequest request) {
        return ApiResponse.<BranchResponse>builder()
                .result(branchService.updateBranch(branchId, request))
                .build();
    }

    @DeleteMapping("/{branchId}")
    ApiResponse<String> deleteBranch(@PathVariable Integer branchId) {
        branchService.deleteBranch(branchId);
        return ApiResponse.<String>builder().result("Branch has been deleted").build();
    }
}
