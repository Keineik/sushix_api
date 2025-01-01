package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "IssuedDate", insertable = false)
    Instant issuedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CardType", insertable = false)
    CardType cardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustID")
    Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StaffID")
    Staff staff;

    @Column(name = "Points", insertable = false)
    Integer points;
}
