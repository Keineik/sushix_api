package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.DepartmentRequest;
import com.group09.sushix_api.dto.response.DepartmentResponse;
import com.group09.sushix_api.entity.Department;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    @Mapping(target = "branchId", source = "branch.branchId")
    DepartmentResponse toDepartmentResponse(Department department);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartment(@MappingTarget Department department, DepartmentRequest request);
}
