package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "BranchMenuItem")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchMenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BranchID", nullable = false)
    Integer branchId;

    @Column(name = "ItemID", nullable = false)
    Integer itemId;

    @Column(name = "IsShippable", nullable = false)
    Boolean isShippable;
}
