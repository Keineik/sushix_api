package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reservation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RsID")
    Integer rsId;

    @Column(name = "NumOfGuests", nullable = false)
    Integer numOfGuests;

    @Column(name = "RsDateTime", columnDefinition = "DATETIME DEFAULT GETDATE()")
    LocalDateTime rsDateTime;

    @Column(name = "ArrivalDateTime", nullable = false)
    LocalDateTime arrivalDateTime;

    @Column(name = "RsNotes", length = 2047)
    String rsNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BranchID", nullable = false, referencedColumnName = "BranchID")
    Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustID", nullable = false, referencedColumnName = "CustID")
    Customer customer;

    @Column(name = "RsStatus", nullable = false, insertable = false)
    String rsStatus;
}