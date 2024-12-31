package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.DepartmentRequest;
import com.group09.sushix_api.dto.response.DepartmentResponse;
import com.group09.sushix_api.entity.Branch;
import com.group09.sushix_api.entity.Department;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.DepartmentMapper;
import com.group09.sushix_api.repository.BranchRepository;
import com.group09.sushix_api.repository.DepartmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService {
    DepartmentRepository departmentRepository;
    DepartmentMapper departmentMapper;

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository
                .findAll()
                .stream()
                .map(departmentMapper::toDepartmentResponse)
                .toList();
    }

    public DepartmentResponse getDepartment(Integer deptId) {
        return departmentMapper.toDepartmentResponse(
                departmentRepository
                        .findById(deptId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));
    }


    public DepartmentResponse updateDepartment(Integer deptId, DepartmentRequest request) {
        Department department = departmentRepository
                .findById(deptId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        departmentMapper.updateDepartment(department, request);

        return departmentMapper.toDepartmentResponse(departmentRepository.save(department));
    }
}
