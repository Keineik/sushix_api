package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "Department")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DeptID")
    Integer deptId;

    @ManyToOne
    @JoinColumn(name = "BranchID", referencedColumnName = "BranchID", updatable = false)
    Branch branch;

    @Column(name = "DeptName", nullable = false)
    String deptName;

    @Column(name = "Salary", precision = 19, scale = 4)
    BigDecimal salary;
}
