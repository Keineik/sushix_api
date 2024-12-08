package com.group09.sushix_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRequest {
    @Size(max = 255)
    String custName;

    @Email
    @Size(max = 255)
    String custEmail;

    @Size(max = 1)
    String custGender;

    @Size(max = 20)
    String custPhoneNumber;

    @Size(max = 20)
    String custCitizenId;
}
