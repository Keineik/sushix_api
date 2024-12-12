package com.group09.sushix_api.service;

import java.util.List;

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

    public List<MenuItemResponse> fetchItems(
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

        System.out.println("Fetch Parameters:");
        System.out.println("Page: " + page);
        System.out.println("Limit: " + limit);
        System.out.println("Search Term: " + searchTerm);
        System.out.println("Category ID: " + categoryId);
        System.out.println("Branch ID: " + branchId);
        System.out.println("Sort Key: " + sortKey);
        System.out.println("Sort Direction: " + sortDirection);

        System.out.println("Total Items Fetched: " + items.size());

        for (MenuItem item : items) {
            System.out.println("Item Details:");
            System.out.println("  Item ID: " + item.getItemId());
            System.out.println("  Item Name: " + item.getItemName());
            System.out.println("  Unit Price: " + item.getUnitPrice());
            System.out.println("  Serving Unit: " + item.getServingUnit());
            System.out.println("  Category ID: " + item.getCategoryId());
            System.out.println("  Is Discontinued: " + item.getIsDiscontinued());
            System.out.println("  Image URL: " + item.getImgUrl());
            System.out.println("---");
        }

        return items.stream().map(menuItemMapper::toMenuItemResponse).toList();
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