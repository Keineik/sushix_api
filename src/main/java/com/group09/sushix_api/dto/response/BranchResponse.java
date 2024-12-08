package com.group09.sushix_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponse {
    Integer branchId;

    String branchName;

    String branchAddress;

    String branchRegion;

    String openingTime;

    String closingTime;

    String branchPhoneNumber;

    Boolean hasCarParking;

    Boolean hasBikeParking;

    String imgUrl;
}