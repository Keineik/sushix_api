package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.CardTypeRequest;
import com.group09.sushix_api.dto.response.CardTypeResponse;
import com.group09.sushix_api.entity.CardType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CardTypeMapper {
    CardTypeResponse toCardTypeResponse(CardType cardType);

    CardType toCardType(CardTypeRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCardType(@MappingTarget CardType cardType, CardTypeRequest request);
}
