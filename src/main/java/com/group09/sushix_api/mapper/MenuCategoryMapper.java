package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.response.MenuCategoryResponse;
import com.group09.sushix_api.entity.MenuCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuCategoryMapper {
    MenuCategoryResponse toMenuCategoryResponse(MenuCategory menuCategory);
}
