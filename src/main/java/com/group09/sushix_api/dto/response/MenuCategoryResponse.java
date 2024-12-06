package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuCategoryResponse {
    Integer id;
    String categoryName;
}
