package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "StaffInfo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffInfo {
    @Id
    @Column(name = "StaffID")
    Integer staffId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "StaffName", nullable = false)
    String staffName;

    @Column(name = "StaffDOB")
    LocalDate staffDOB;

    @Size(max = 1)
    @Column(name = "StaffGender", nullable = false)
    String staffGender;

    @Column(name = "StaffPhoneNumber", nullable = false)
    String staffPhoneNumber;

    @Column(name = "StaffCitizenID", nullable = false)
    String staffCitizenId;
}