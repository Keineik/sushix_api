package com.group09.sushix_api.repository;

import com.group09.sushix_api.dto.ReservationsDTO;
import com.group09.sushix_api.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Procedure(procedureName = "usp_InsertReservation")
    Integer insertReservation(
            @Param("NumOfGuests") Integer numOfGuests,
            @Param("ArrivalDateTime") String arrivalDateTime,
            @Param("RsNotes") String rsNotes,
            @Param("BranchID") Integer branchId,
            @Param("CustID") Integer custId
    );

    @Procedure(procedureName = "usp_FetchReservations")
    List<ReservationsDTO> fetchReservations(
            @Param("Page") Integer page,
            @Param("Limit") Integer limit,
            @Param("SearchTerm") String searchTerm,
            @Param("Status") String status,
            @Param("CustID") Integer custId,
            @Param("BranchID") Integer branchId,
            @Param("SortDirection") Boolean sortDirection
    );

    @Procedure(procedureName = "usp_FetchReservations_count")
    Integer fetchReservationsCount(
            @Param("SearchTerm") String searchTerm,
            @Param("Status") String status,
            @Param("BranchID") Integer branchId
    );
}
