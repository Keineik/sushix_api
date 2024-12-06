package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Integer> {
}
