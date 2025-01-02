package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Integer> {
    @NativeQuery("SELECT * FROM [Table] WHERE BranchID = :branchId")
    List<RestaurantTable> getRestaurantTableByBranchID(Integer branchId);
}