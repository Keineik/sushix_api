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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "BranchID", referencedColumnName = "BranchID"),
            @JoinColumn(name = "DeptName", referencedColumnName = "DeptName")
    })
    Department department;

    @Column(name = "isBranchManager")
    Boolean isBranchManager = false;
}
