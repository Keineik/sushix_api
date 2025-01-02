package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.BranchMenuItemRequest;
import com.group09.sushix_api.dto.response.BranchMenuItemResponse;
import com.group09.sushix_api.entity.BranchMenuItem;
import org.mapstruct.*;
import org.springframework.context.annotation.Bean;

@Mapper(componentModel = "spring")
public interface BranchMenuItemMapper {
    BranchMenuItemResponse toBranchMenuItemResponse(BranchMenuItem branchMenuItem);

    BranchMenuItem toBranchMenuItem(BranchMenuItemRequest branchMenuItemRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBranchMenuItem(@MappingTarget BranchMenuItem branchMenuItem, BranchMenuItemRequest branchMenuItemRequest);
}
