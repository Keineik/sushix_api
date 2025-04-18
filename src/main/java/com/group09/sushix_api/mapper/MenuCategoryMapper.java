package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.MenuCategoryRequest;
import com.group09.sushix_api.dto.response.MenuCategoryResponse;
import com.group09.sushix_api.entity.MenuCategory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MenuCategoryMapper {
    MenuCategoryResponse toMenuCategoryResponse(MenuCategory menuCategory);

    MenuCategory toMenuCategory(MenuCategoryRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMenuCategory(@MappingTarget MenuCategory menuCategory, MenuCategoryRequest request);
}
