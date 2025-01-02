package com.group09.sushix_api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchMenuItemRequest {
    Integer branchId;
    Integer itemId;
    Boolean isShippable;
}
