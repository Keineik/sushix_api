package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "OnlineAccess")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OnlineAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccessID")
    Integer accessId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustID", nullable = false, referencedColumnName = "CustID")
    Customer customer;

    @Column(name = "StartDateTime")
    Instant startDateTime;

    @Column(name = "EndDateTime")
    Instant endDateTime;
}
