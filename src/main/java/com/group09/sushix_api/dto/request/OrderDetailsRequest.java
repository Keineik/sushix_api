package com.group09.sushix_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailsRequest {
    @NotNull
    Integer itemId;

    @NotNull
    @Min(1)
    Integer quantity;
}
