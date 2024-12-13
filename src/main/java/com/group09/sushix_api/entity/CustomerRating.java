package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "CustomerRating")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RatingID")
    Integer ratingId;

    @Column(name = "ServiceRating", nullable = false)
    Integer serviceRating;

    @Column(name = "LocationRating", nullable = false)
    Integer locationRating;

    @Column(name = "FoodRating", nullable = false)
    Integer foodRating;

    @Column(name = "PricingRating", nullable = false)
    Integer pricingRating;

    @Column(name = "AmbianceRating", nullable = false)
    Integer ambianceRating;

    @Column(name = "FeedbackCmt")
    String feedbackCmt;

    @Column(name = "FeedbackDate", nullable = false)
    LocalDate feedbackDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "InvoiceID", unique = true, nullable = false)
    Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BranchID")
    Branch branch;
}