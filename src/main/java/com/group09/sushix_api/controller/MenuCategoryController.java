package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.MenuCategoryResponse;
import com.group09.sushix_api.service.MenuCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
