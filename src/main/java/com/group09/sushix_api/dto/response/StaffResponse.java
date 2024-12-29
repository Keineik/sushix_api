package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffResponse {
    Integer staffId;
    String deptName;
    String branchId;
    String staffName;
    String staffDOB;
    String staffGender;
    String staffPhoneNumber;
    String staffCitizenId;
}
