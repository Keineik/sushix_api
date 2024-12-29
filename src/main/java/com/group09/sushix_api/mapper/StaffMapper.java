package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.StaffRequest;
import com.group09.sushix_api.dto.response.StaffResponse;
import com.group09.sushix_api.entity.Staff;
import com.group09.sushix_api.entity.StaffInfo;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    @Mapping(target = "staffId", source = "staffInfo.staffId")
    StaffResponse toStaffResponse(Staff staff, StaffInfo staffInfo);

    Staff toStaff(StaffRequest request);

    StaffInfo toStaffInfo(StaffRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStaff(@MappingTarget Staff staff, StaffRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStaffInfo(@MappingTarget StaffInfo staffInfo, StaffRequest request);
}
