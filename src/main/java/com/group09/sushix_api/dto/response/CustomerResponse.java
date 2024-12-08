package com.group09.sushix_api.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {
    Integer custId;
    String custName;
    String custEmail;
    String custGender;
    String custPhoneNumber;
    String custCitizenId;
}
