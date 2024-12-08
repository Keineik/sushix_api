package com.group09.sushix_api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchResponse {
    Integer branchId;
    String branchName;
    String branchAddress;
    String branchRegion;
    LocalTime openingTime;
    LocalTime closingTime;
    String branchPhoneNumber;
    Boolean hasCarParking;
    Boolean hasBikeParking;
    String imgUrl;
}