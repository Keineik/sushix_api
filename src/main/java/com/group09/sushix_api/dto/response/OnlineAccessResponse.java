package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.String;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OnlineAccessResponse {
    Integer accessId;
    Integer custId;
    String startDateTime;
    String endDateTime;
}
