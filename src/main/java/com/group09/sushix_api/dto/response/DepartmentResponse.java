package com.group09.sushix_api.dto.response;

import com.group09.sushix_api.entity.Branch;
import com.group09.sushix_api.entity.Department;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentResponse {
    Integer deptId;
    Integer branchId;
    String deptName;
    BigDecimal salary;
}
