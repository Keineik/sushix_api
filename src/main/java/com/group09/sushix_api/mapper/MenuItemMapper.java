package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.MenuItemRequest;
import com.group09.sushix_api.dto.response.MenuItemResponse;
import com.group09.sushix_api.entity.MenuItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    @Mapping(target = "categoryId", source = "categoryId.categoryId")
    MenuItemResponse toMenuItemResponse(MenuItem menuItem);

    @Mapping(target = "categoryId", ignore = true)
    MenuItem toMenuItem(MenuItemRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "categoryId", ignore = true)
    void updateMenuItem(@MappingTarget MenuItem menuItem, MenuItemRequest request);
}