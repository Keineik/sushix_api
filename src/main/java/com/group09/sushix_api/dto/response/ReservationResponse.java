package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationResponse {
    Integer rsId;
    Integer numOfGuests;
    LocalDateTime rsDateTime;
    LocalDateTime arrivalDateTime;
    String rsNotes;
    Integer branchId;
    Integer custId;
    String rsStatus;
}
