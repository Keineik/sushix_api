package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "StaffRating")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StaffRatingID")
    Integer staffRatingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RatingID")
    CustomerRating customerRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StaffID")
    Staff staff;

    @Column(name = "StaffRating", nullable = false)
    Integer staffRating;
}
