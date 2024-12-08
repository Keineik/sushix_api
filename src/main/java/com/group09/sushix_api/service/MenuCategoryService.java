package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.response.MenuCategoryResponse;
import com.group09.sushix_api.mapper.MenuCategoryMapper;
import com.group09.sushix_api.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MenuCategoryService {
    MenuCategoryRepository menuCategoryRepository;
    MenuCategoryMapper menuCategoryMapper;

    public List<MenuCategoryResponse> getAllMenuCategories() {
        return menuCategoryRepository
                .findAll()
                .stream()
                .map(menuCategoryMapper::toMenuCategoryResponse)
                .toList();
    }
}
