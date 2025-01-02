package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.BranchMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;

public interface BranchMenuItemRepository extends JpaRepository<BranchMenuItem, Integer> {
    @NativeQuery("SELECT * FROM BranchMenuItem WHERE BranchID = :branchId")
    List<BranchMenuItem> getAllBranchMenuItems(Integer branchId);

    @NativeQuery("SELECT * FROM BranchMenuItem WHERE BranchID = :branchID AND ItemID = :itemId")
    BranchMenuItem getBranchMenuItem(Integer branchId, Integer itemId);

    @Modifying
    @NativeQuery("DELETE BranchMenuItem WHERE BranchID = :branchID AND ItemID = :itemId")
    void deleteBranchMenuItem(Integer branchId, Integer itemId);
}
