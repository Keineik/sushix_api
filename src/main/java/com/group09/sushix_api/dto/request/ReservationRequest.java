package com.group09.sushix_api.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationRequest {
    String custName;

    String custEmail;

    String custPhoneNumber;

    @Min(1)
    Integer numOfGuests;

    @Future
    String arrivalDateTime;

    @Size(max = 2047)
    String rsNotes;

    Integer branchId;

    Integer custId;

    String rsStatus;

    Set<OrderDetailsRequest> orderDetails;
}
