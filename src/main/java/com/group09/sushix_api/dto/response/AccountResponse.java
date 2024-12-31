package com.group09.sushix_api.dto.response;

import com.group09.sushix_api.entity.Customer;
import com.group09.sushix_api.entity.Staff;
import com.group09.sushix_api.entity.StaffInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    String accountId;
    String username;
    String password;
    Customer customer;
    Staff staff;
    StaffInfo staffInfo;
    Boolean isAdmin;
}
