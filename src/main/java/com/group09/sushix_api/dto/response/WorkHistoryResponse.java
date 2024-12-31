package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkHistoryResponse {
    Integer staffId;
    String startDate;
    Integer branchId;
    String deptName;
    String quitDate;
}
