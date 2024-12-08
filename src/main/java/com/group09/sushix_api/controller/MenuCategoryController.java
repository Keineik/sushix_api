package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.MenuCategoryRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.MenuCategoryResponse;
import com.group09.sushix_api.service.MenuCategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu-category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MenuCategoryController {
    MenuCategoryService menuCategoryService;

    @GetMapping
    ApiResponse<List<MenuCategoryResponse>> getMenuCategories() {
        return ApiResponse.<List<MenuCategoryResponse>>builder()
                .result(menuCategoryService.getAllMenuCategories())
                .build();
    }

    @GetMapping("/{categoryId}")
    ApiResponse<MenuCategoryResponse> getMenuCategory(@PathVariable("categoryId") Integer categoryId) {
        return ApiResponse.<MenuCategoryResponse>builder()
                .result(menuCategoryService.getMenuCategory(categoryId))
                .build();
    }

    @PostMapping
    ApiResponse<MenuCategoryResponse> createMenuCategory(@RequestBody @Valid MenuCategoryRequest request) {
        return ApiResponse.<MenuCategoryResponse>builder()
                .result(menuCategoryService.createMenuCategory(request))
                .build();
    }

    @PutMapping("/{categoryId}")
    ApiResponse<MenuCategoryResponse> updateMenuCategory(@PathVariable("categoryId") Integer categoryId,
                                                         @RequestBody MenuCategoryRequest request) {
        return ApiResponse.<MenuCategoryResponse>builder()
                .result(menuCategoryService.updateMenuCategory(categoryId, request))
                .build();
    }

    @DeleteMapping("/{categoryId}")
    ApiResponse<String> deleteMenuCategory(@PathVariable Integer categoryId) {
        menuCategoryService.deleteMenuCategory(categoryId);
        return ApiResponse.<String>builder().result("Menu category has been deleted").build();
    }
}
