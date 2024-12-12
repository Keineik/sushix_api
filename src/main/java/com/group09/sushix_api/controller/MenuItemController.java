package com.group09.sushix_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group09.sushix_api.dto.request.MenuItemRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.MenuItemResponse;
import com.group09.sushix_api.entity.MenuItem;
import com.group09.sushix_api.service.MenuItemService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/menu-item")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MenuItemController {
    MenuItemService menuItemService;

    @GetMapping
    public ApiResponse<List<MenuItemResponse>> fetchItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "18") int limit,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") int categoryId,
            @RequestParam(defaultValue = "0") int branchId,
            @RequestParam(defaultValue = "ID") String sortKey,
            @RequestParam(defaultValue = "false") boolean sortDirection
    ) {
        return ApiResponse.<List<MenuItemResponse>>builder()
                .result(menuItemService.fetchItems(
                        page,
                        limit,
                        searchTerm,
                        categoryId,
                        branchId,
                        sortKey,
                        sortDirection
                ))
                .build();
    }

    @GetMapping("/{itemId}")
    ApiResponse<MenuItemResponse> getMenuItem(@PathVariable("itemId") Integer itemId) {
        return ApiResponse.<MenuItemResponse>builder()
                .result(menuItemService.getMenuItem(itemId))
                .build();
    }

    @PostMapping
    ApiResponse<MenuItemResponse> createMenuItem(@RequestBody @Valid MenuItemRequest request) {
        return ApiResponse.<MenuItemResponse>builder()
                .result(menuItemService.createMenuItem(request))
                .build();
    }

    @PutMapping("/{itemId}")
    ApiResponse<MenuItemResponse> updateMenuItem(@PathVariable("itemId") Integer itemId,
                                                 @RequestBody MenuItemRequest request) {
        return ApiResponse.<MenuItemResponse>builder()
                .result(menuItemService.updateMenuItem(itemId, request))
                .build();
    }

    @DeleteMapping("/{itemId}")
    ApiResponse<String> deleteMenuItem(@PathVariable Integer itemId) {
        menuItemService.deleteMenuItem(itemId);
        return ApiResponse.<String>builder().result("Menu item has been deleted").build();
    }
}