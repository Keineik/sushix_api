package com.group09.sushix_api.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.group09.sushix_api.dto.request.MenuItemRequest;
import com.group09.sushix_api.dto.response.MenuItemResponse;
import com.group09.sushix_api.entity.MenuItem;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.MenuItemMapper;
import com.group09.sushix_api.repository.MenuCategoryRepository;
import com.group09.sushix_api.repository.MenuItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MenuItemService {
    MenuItemRepository menuItemRepository;
    MenuItemMapper menuItemMapper;
    MenuCategoryRepository menuCategoryRepository;

    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemRepository
                .findAll()
                .stream()
                .map(menuItemMapper::toMenuItemResponse)
                .toList();
    }

    @Transactional
    public Map<String, Object> fetchItems(
            int page,
            int limit,
            String searchTerm,
            int categoryId,
            int branchId,
            String sortKey,
            boolean sortDirection
    ) {
        List<MenuItem> items = menuItemRepository.fetchItems(
                page,
                limit,
                searchTerm,
                categoryId,
                branchId,
                sortKey,
                sortDirection
        );

        int totalCount = menuItemRepository.countItems(
                searchTerm,
                categoryId,
                branchId
        );
        List<MenuItemResponse> menuItemResponses = items.stream()
                .map(menuItemMapper::toMenuItemResponse)
                .toList();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("items", menuItemResponses);
        responseMap.put("totalCount", totalCount);

        return responseMap;
    }

    public MenuItemResponse getMenuItem(Integer itemId) {
        return menuItemMapper.toMenuItemResponse(
                menuItemRepository
                        .findById(itemId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));
    }

    public MenuItemResponse createMenuItem(MenuItemRequest request) {
        MenuItem menuItem = menuItemMapper.toMenuItem(request);
        menuItem.setCategoryId(
                menuCategoryRepository
                        .findById(request.getCategoryId())
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));

        return menuItemMapper.toMenuItemResponse(menuItemRepository.save(menuItem));
    }

    public MenuItemResponse updateMenuItem(Integer itemId, MenuItemRequest request) {
        MenuItem menuItem = menuItemRepository
                .findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        menuItemMapper.updateMenuItem(menuItem, request);
        menuItem.setCategoryId(
                menuCategoryRepository
                        .findById(request.getCategoryId())
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));

        return menuItemMapper.toMenuItemResponse(menuItemRepository.save(menuItem));
    }

    public void deleteMenuItem(Integer itemId) {
        menuItemRepository.deleteById(itemId);
    }


}