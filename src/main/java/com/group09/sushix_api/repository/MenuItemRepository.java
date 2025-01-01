package com.group09.sushix_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group09.sushix_api.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    @Procedure(procedureName = "usp_FetchItems")
    List<MenuItem> fetchItems(
            @Param("Page") Integer page,
            @Param("Limit") Integer limit,
            @Param("SearchTerm") String searchTerm,
            @Param("CategoryID") Integer categoryId,
            @Param("BranchID") Integer branchId,
            @Param("FilterShippable") Boolean filterShippable,
            @Param("SortKey") String sortKey,
            @Param("SortDirection") Boolean sortDirection
    );

    @Procedure(procedureName = "usp_FetchItems_count")
    int countItems(
            @Param("SearchTerm") String searchTerm,
            @Param("CategoryID") Integer categoryId,
            @Param("BranchID") Integer branchId,
            @Param("FilterShippable") Boolean filterShippable
    );
}