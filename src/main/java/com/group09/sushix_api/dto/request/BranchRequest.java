package com.group09.sushix_api.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchRequest {
    @Size(max = 100)
    String branchName;

    @Size(max = 255)
    String branchAddress;

    @Size(max = 50)
    String branchRegion;

    LocalTime openingTime;

    LocalTime closingTime;

    @Size(max = 25)
    String branchPhoneNumber;

    Boolean hasCarParking;

    Boolean hasBikeParking;

    @Size(max = 2083)
    String imgUrl;
}
