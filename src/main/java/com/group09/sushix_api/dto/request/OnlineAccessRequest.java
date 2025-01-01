package com.group09.sushix_api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.lang.String;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OnlineAccessRequest {
    Integer custId;
    String startDateTime;
    String endDateTime;
}
