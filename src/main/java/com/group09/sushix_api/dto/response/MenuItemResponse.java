package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItemResponse {
    Integer itemId;
    String itemName;
    BigDecimal unitPrice;
    String servingUnit;
    Integer categoryId;
    Integer soldQuantity;
    Boolean isDiscontinued;
    String imgUrl;
}