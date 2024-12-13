package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "[Table]")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TableID")
    Integer tableId;

    @Column(name = "TableCode", nullable = false)
    Integer tableCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BranchID", nullable = false)
    Branch branch;

    @Column(name = "NumOfSeats")
    Integer numOfSeats;

    @Column(name = "isVacant", columnDefinition = "BIT DEFAULT 0")
    Boolean isVacant;
}