package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "WorkHistory")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkHistory {
    @Id
    @Column(name = "StaffID", nullable = false)
    Integer staffId;

    @Column(name = "StartDate", nullable = false)
    Instant startDate;

    @JoinColumn(name = "BranchID", nullable = false)
    Integer branchId;

    @Column(name = "DeptName", nullable = false)
    String deptName;

    @Column(name = "QuitDate")
    Instant quitDate;
}
