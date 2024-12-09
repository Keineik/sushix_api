package com.group09.sushix_api.mapper;


import com.group09.sushix_api.dto.request.MembershipCardRequest;
import com.group09.sushix_api.dto.response.MembershipCardResponse;
import com.group09.sushix_api.entity.MembershipCard;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MembershipCardMapper {
    @Mapping(target = "cardTypeId", source = "cardType.cardTypeId")
    @Mapping(target = "custId", source = "customer.custId")
    MembershipCardResponse toMembershipCardResponse(MembershipCard membershipCard);

    @Mapping(target = "cardType", ignore = true)
    @Mapping(target = "customer", ignore = true)
    MembershipCard toMembershipCard(MembershipCardRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cardType", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateMembershipCard(@MappingTarget MembershipCard membershipCard, MembershipCardRequest request);
}
