package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Customer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustID")
    Integer custId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "CustName", nullable = false)
    String custName;

    @Email
    @Size(max = 255)
    @Column(name = "CustEmail")
    String custEmail;

    @Column(name = "CustGender", length = 1)
    String custGender;

    @Size(max = 20)
    @Column(name = "CustPhoneNumber", length = 20)
    String custPhoneNumber;

    @Size(max = 20)
    @Column(name = "CustCitizenID", unique = true, length = 20)
    String custCitizenId;
}
