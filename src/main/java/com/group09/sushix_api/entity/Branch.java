package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Entity
@Table(name = "Branch")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BranchID")
    Integer branchId;

    @Column(name = "BranchName", nullable = false, length = 100)
    String branchName;

    @Column(name = "BranchAddress", nullable = false)
    String branchAddress;

    @Column(name = "BranchRegion", length = 50)
    String branchRegion;

    @Column(name = "OpeningTime")
    LocalTime openingTime;

    @Column(name = "ClosingTime")
    LocalTime closingTime;

    @Column(name = "BranchPhoneNumber", length = 25)
    String branchPhoneNumber;

    @Column(name = "HasCarParking")
    Boolean hasCarParking;

    @Column(name = "HasBikeParking")
    Boolean hasBikeParking;

    @Column(name = "ImgUrl", length = 2083)
    String imgUrl;
}
