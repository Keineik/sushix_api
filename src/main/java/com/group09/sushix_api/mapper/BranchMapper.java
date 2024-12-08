package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.BranchRequest;
import com.group09.sushix_api.dto.response.BranchResponse;
import com.group09.sushix_api.entity.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    BranchResponse toBranchResponse(Branch branch);

    Branch toBranch(BranchRequest request);

    void updateBranch(@MappingTarget Branch branch, BranchRequest request);
}
