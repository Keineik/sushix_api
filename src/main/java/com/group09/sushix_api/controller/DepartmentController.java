package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.DepartmentRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.DepartmentResponse;
import com.group09.sushix_api.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DepartmentController {
    DepartmentService departmentService;

    @GetMapping("/distinct")
    ApiResponse<List<String>> getAllDistinctDepartments() {
        return ApiResponse.<List<String>>builder()
                .result(departmentService.getAllDistinctDeptName())
                .build();
    }

    @GetMapping
    ApiResponse<List<DepartmentResponse>> getAllDepartments() {
        return ApiResponse.<List<DepartmentResponse>>builder()
                .result(departmentService.getAllDepartments())
                .build();
    }

    @GetMapping("/{deptId}")
    ApiResponse<DepartmentResponse> getDepartment(@PathVariable("deptId") Integer deptId) {
        return ApiResponse.<DepartmentResponse>builder()
                .result(departmentService.getDepartment(deptId))
                .build();
    }

    @PutMapping("/{deptId}")
    ApiResponse<DepartmentResponse> updateDepartment(
            @PathVariable("deptId") Integer deptId,
            @RequestBody @Valid DepartmentRequest request) {
        return ApiResponse.<DepartmentResponse>builder()
                .result(departmentService.updateDepartment(deptId, request))
                .build();
    }
}