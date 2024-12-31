package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.WorkHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;

public interface WorkHistoryRepository extends JpaRepository<WorkHistory, Integer> {
    @NativeQuery(value = "SELECT * FROM WorkHistory WHERE StaffID = :staffId ORDER BY startDate")
    List<WorkHistory> getStaffWorkHistory(Integer staffId);
}
