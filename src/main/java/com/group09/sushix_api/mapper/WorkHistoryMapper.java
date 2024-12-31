package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.response.WorkHistoryResponse;
import com.group09.sushix_api.entity.WorkHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkHistoryMapper {
    WorkHistoryResponse toWorkHistoryResponse(WorkHistory workHistory);
}
