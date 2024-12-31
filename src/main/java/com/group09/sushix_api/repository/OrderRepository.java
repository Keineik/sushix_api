package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Procedure(procedureName = "usp_InsertDineInOrder")
    Integer insertDineInOrder(
            @Param("CustID") Integer custId,
            @Param("BranchID") Integer branchId,
            @Param("StaffID") Integer staffId,
            @Param("TableCode") Integer tableCode,
            @Param("RsID") Integer rsId
    );

    @Procedure(procedureName = "usp_InsertDeliveryOrder")
    Integer insertDeliveryOrder(
            @Param("CustID") Integer custId,
            @Param("BranchID") Integer branchId,
            @Param("DeliveryAddress") String deliveryAddress,
            @Param("DeliveryDateTime") String deliveryDateTime
    );

    @Procedure(procedureName = "usp_InsertOrderDetails")
    void insertOrderDetails(
            @Param("OrderID") Integer orderId,
            @Param("ItemID") Integer itemId,
            @Param("Quantity") Integer quantity
    );

    @Modifying
    @NativeQuery(value = "DELETE FROM [Order] WHERE OrderID = :orderId")
    void myDeleteById(Integer orderId);
}
