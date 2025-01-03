package com.group09.sushix_api.repository;

import com.group09.sushix_api.dto.InvoiceDTO;
import com.group09.sushix_api.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Procedure(procedureName = "usp_CreateInvoice")
    Integer createInvoice(
            @Param("OrderID") Integer orderId,
            @Param("PaymentMethod") String paymentMethod,
            @Param("TaxRate") Float taxRate,
            @Param("CouponCode") String couponCode
    );

    @Procedure(procedureName = "usp_FetchInvoices")
    List<InvoiceDTO> fetchInvoices(
            @Param("Page") Integer page,
            @Param("Limit") Integer limit,
            @Param("SearchTerm") String searchTerm,
            @Param("BranchID") Integer branchId,
            @Param("CustID") Integer custId,
            @Param("StartDate") String startDate,
            @Param("EndDate") String endDate,
            @Param("SortDirection") Boolean sortDirection
    );

    @Procedure(procedureName = "usp_FetchInvoices_count")
    Integer fetchInvoicesCount(
            @Param("SearchTerm") String searchTerm,
            @Param("BranchID") Integer branchId,
            @Param("StartDate") String startDate,
            @Param("EndDate") String endDate
    );
}