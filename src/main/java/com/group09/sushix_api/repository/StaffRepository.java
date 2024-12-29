package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    @Procedure(procedureName = "TransferStaff")
    void transferStaff(
            @Param("StaffID") Integer staffId,
            @Param("NewBranchID") Integer newBranchId,
            @Param("NewDeptName") String newDeptName,
            @Param("StartDate") String startDate
    );
}
