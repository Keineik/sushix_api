package com.group09.sushix_api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.group09.sushix_api.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    //@Query(value = "EXEC usp_FetchItems :Page, :Limit, :SearchTerm, :CategoryID, :BranchID, :SortKey, :SortDirection", nativeQuery = true)
    @Procedure(procedureName = "usp_FetchItems")
    List<MenuItem> fetchItems(
            @Param("Page") int page,
            @Param("Limit") int limit,
            @Param("SearchTerm") String searchTerm,
            @Param("CategoryID") int categoryId,
            @Param("BranchID") int branchId,
            @Param("SortKey") String sortKey,
            @Param("SortDirection") boolean sortDirection
    );
}