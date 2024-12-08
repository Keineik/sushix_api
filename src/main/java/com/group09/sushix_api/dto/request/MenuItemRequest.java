package com.group09.sushix_api.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItemRequest {
    @Size(max = 100)
    String itemName;

    BigDecimal unitPrice;

    @Size(max = 10)
    String servingUnit;

    Integer categoryId;

    Boolean isDiscontinued;

    @Size(max = 2083)
    String imgUrl;
}