package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Account")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID")
    Integer accountId;

    @Column(name = "Username", nullable = false, unique = true, length = 100)
    String username;

    @Column(name = "Password", nullable = false)
    String password;

    @ManyToOne
    @JoinColumn(name = "CustID", referencedColumnName = "CustID")
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "StaffID", referencedColumnName = "StaffID")
    Staff staff;

    @Column(name = "IsAdmin", nullable = false)
    Boolean isAdmin;
}
