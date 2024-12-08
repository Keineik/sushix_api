package com.group09.sushix_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchRequest {
    @NotBlank
    @Size(max = 100)
    String branchName;

    @NotBlank
    @Size(max = 255)
    String branchAddress;

    @Size(max = 50)
    String branchRegion;

    String openingTime; // Assuming time input is in String (e.g., "08:00:00")

    String closingTime; // Same assumption as openingTime

    @Size(max = 25)
    String branchPhoneNumber;

    Boolean hasCarParking;

    Boolean hasBikeParking;

    @Size(max = 2083)
    String imgUrl;
}
