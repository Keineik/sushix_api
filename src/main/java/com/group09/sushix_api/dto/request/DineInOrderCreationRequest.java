package com.group09.sushix_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DineInOrderCreationRequest {
    Integer custId;
    Integer branchId;
    Integer tableCode;
    Integer rsId;
    Set<OrderDetailsRequest> orderDetails;
}
