package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.MenuItemRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.MenuItemResponse;
import com.group09.sushix_api.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/menu-item")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MenuItemController {
    MenuItemService menuItemService;

    @GetMapping
    public ApiResponse<Map<String, Object>> fetchItems(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "18") Integer limit,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") Integer categoryId,
            @RequestParam(defaultValue = "0") Integer branchId,
            @RequestParam(defaultValue = "0") Boolean filterShippable,
            @RequestParam(defaultValue = "ID") String sortKey,
            @RequestParam(defaultValue = "false") Boolean sortDirection
    ) {
        Map<String, Object> result = menuItemService.fetchItems(
                page,
                limit,
                searchTerm,
                categoryId,
                branchId,
                filterShippable,
                sortKey,
                sortDirection
        );

        return ApiResponse.<Map<String, Object>>builder()
                .result(result)
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