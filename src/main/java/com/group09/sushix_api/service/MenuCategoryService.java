package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.MenuCategoryRequest;
import com.group09.sushix_api.dto.response.MenuCategoryResponse;
import com.group09.sushix_api.dto.response.MenuItemResponse;
import com.group09.sushix_api.entity.MenuCategory;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.MenuCategoryMapper;
import com.group09.sushix_api.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
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

    public MenuCategoryResponse getMenuCategory(Integer categoryId) {
        return menuCategoryMapper.toMenuCategoryResponse(
                menuCategoryRepository
                        .findById(categoryId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));
    }

    public MenuCategoryResponse createMenuCategory(MenuCategoryRequest request) {
        MenuCategory menuCategory = menuCategoryMapper.toMenuCategory(request);

        return menuCategoryMapper.toMenuCategoryResponse(menuCategoryRepository.save(menuCategory));
    }

    public MenuCategoryResponse updateMenuCategory(Integer categoryId, MenuCategoryRequest request) {
        MenuCategory menuCategory = menuCategoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        menuCategoryMapper.updateMenuCategory(menuCategory, request);

        return menuCategoryMapper.toMenuCategoryResponse(menuCategoryRepository.save(menuCategory));
    }

    public void deleteMenuCategory(Integer categoryId) {
        menuCategoryRepository.deleteById(categoryId);
    }
}
