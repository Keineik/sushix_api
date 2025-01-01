package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.OnlineAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OnlineAccessRepository extends JpaRepository<OnlineAccess, Integer> {
    @Procedure(procedureName = "usp_LogCustomerOnlineAccess")
    Integer logCustomerOnlineAccess(
            @Param("CustID") Integer custId,
            @Param("IsStart") Boolean isStart
    );

    @NativeQuery("SELECT * FROM OnlineAccess WHERE CustID = :custId")
    List<OnlineAccess> findAllByCustId(Integer custId);
}
