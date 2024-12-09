package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "Staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StaffID")
    Integer staffId;

    @Size(max = 100)
    @Column(name = "StaffName", nullable = false)
    String staffName;

    @Column(name = "StaffDOB")
    LocalDate staffDOB;

    @Column(name = "StaffGender", length = 1)
    String staffGender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "BranchID", referencedColumnName = "BranchID"),
            @JoinColumn(name = "DeptName", referencedColumnName = "DeptName")
    })
    Department department;

    @Column(name = "isBranchManager", nullable = false)
    Boolean isBranchManager = false;
}
