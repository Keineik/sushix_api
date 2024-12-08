package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.BranchRequest;
import com.group09.sushix_api.dto.response.BranchResponse;
import com.group09.sushix_api.entity.Branch;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.BranchMapper;
import com.group09.sushix_api.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BranchService {
    BranchRepository branchRepository;
    BranchMapper branchMapper;

    public List<BranchResponse> getAllBranches() {
        return branchRepository
                .findAll()
                .stream()
                .map(branchMapper::toBranchResponse)
                .toList();
    }

    public BranchResponse getBranch(Integer branchId) {
        return branchMapper.toBranchResponse(
                branchRepository
                        .findById(branchId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));
    }

    public BranchResponse createBranch(BranchRequest request) {
        Branch branch = branchMapper.toBranch(request);

        return branchMapper.toBranchResponse(branchRepository.save(branch));
    }

    public BranchResponse updateBranch(Integer branchId, BranchRequest request) {
        Branch branch = branchRepository
                .findById(branchId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        branchMapper.updateBranch(branch, request);

        return branchMapper.toBranchResponse(branchRepository.save(branch));
    }

    public void deleteBranch(Integer branchId) {
        branchRepository.deleteById(branchId);
    }
}
