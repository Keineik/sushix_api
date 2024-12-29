package com.group09.sushix_api.repository;

import com.group09.sushix_api.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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
}
