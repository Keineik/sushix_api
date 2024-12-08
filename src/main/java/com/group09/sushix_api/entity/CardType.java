package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "CardType")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CardTypeID")
    Integer cardTypeId;

    @Column(name = "CardName", length = 10)
    String cardName;

    @Column(name = "DiscountRate", precision = 5, scale = 2)
    BigDecimal discountRate;

    @Column(name = "PointsRequiredForRenewal")
    Integer pointsRequiredForRenewal;

    @Column(name = "PointsRequiredForUpgrade")
    Integer pointsRequiredForUpgrade;
}
