package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.OrderDetails;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    @NativeQuery(value = "SELECT od.* FROM OrderDetails od WHERE od.OrderID = :orderId")
    List<OrderDetails> findAllByOrderId(Integer orderId);
}
