package com.group09.sushix_api.repository;

import com.group09.sushix_api.dto.StaffDTO;
import com.group09.sushix_api.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    @Procedure(procedureName = "TransferStaff")
    void transferStaff(
            @Param("StaffID") Integer staffId,
            @Param("NewBranchID") Integer newBranchId,
            @Param("NewDeptName") String newDeptName,
            @Param("StartDate") String startDate
    );

    @Procedure(procedureName = "usp_FetchStaffs")
    List<StaffDTO> fetchStaffs(
            @Param("Page") Integer page,
            @Param("Limit") Integer limit,
            @Param("SearchTerm") String searchTerm,
            @Param("BranchID") Integer branchId,
            @Param("Department") String department
    );

    @Procedure(procedureName = "usp_FetchStaffs_count")
    Integer fetchStaffsCount(
            @Param("SearchTerm") String searchTerm,
            @Param("BranchID") Integer branchId,
            @Param("Department") String department
    );
}
