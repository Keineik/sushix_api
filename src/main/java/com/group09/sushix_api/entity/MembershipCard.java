package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "MembershipCard")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CardID")
    Integer cardId;

    @Column(name = "IssuedDate", nullable = false)
    Instant issuedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CardType", referencedColumnName = "CardTypeID", nullable = false)
    CardType cardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustID", referencedColumnName = "CustID", nullable = false)
    Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StaffID", referencedColumnName = "StaffID", nullable = false)
    Staff staff;

    @Column(name = "Points", nullable = false)
    Integer points;
}
