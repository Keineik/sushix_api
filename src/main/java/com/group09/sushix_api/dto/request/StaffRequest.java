package com.group09.sushix_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffRequest {
    String deptName;
    Integer branchId;
    String staffName;
    String staffDOB;
    String staffGender;
    String staffPhoneNumber;
    String staffCitizenId;
}
