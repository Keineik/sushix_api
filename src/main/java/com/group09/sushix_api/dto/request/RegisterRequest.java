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
public class RegisterRequest {
    @Size(min = 8, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "PASSWORD_TOO_SHORT")
    String phoneNumber;

    @Email
    String email;
    Integer cardId;
    String custName;
    String password;
}
