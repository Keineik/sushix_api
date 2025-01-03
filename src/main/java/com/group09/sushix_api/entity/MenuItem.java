package com.group09.sushix_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "MenuItem")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ItemID")
    Integer itemId;

    @Column(name = "ItemName", nullable = false, length = 100)
    String itemName;

    @Column(name = "UnitPrice", precision = 19, scale = 4)
    BigDecimal unitPrice;

    @Column(name = "ServingUnit", length = 10)
    String servingUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryID", referencedColumnName = "CategoryID")
    MenuCategory categoryId;

    @Column(name = "SoldQuantity", nullable = false)
    Integer soldQuantity;

    @Column(name = "IsDiscontinued", nullable = false)
    Boolean isDiscontinued;

    @Column(name = "ImgUrl", length = 2083)
    String imgUrl;
}