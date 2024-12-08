package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.CardTypeRequest;
import com.group09.sushix_api.dto.response.CardTypeResponse;
import com.group09.sushix_api.entity.CardType;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.CardTypeMapper;
import com.group09.sushix_api.repository.CardTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CardTypeService {

    CardTypeRepository cardTypeRepository;
    CardTypeMapper cardTypeMapper;

    public List<CardTypeResponse> getAllCardTypes() {
        return cardTypeRepository
                .findAll()
                .stream()
                .map(cardTypeMapper::toCardTypeResponse)
                .toList();
    }

    public CardTypeResponse getCardType(Integer cardTypeId) {
        return cardTypeMapper.toCardTypeResponse(
                cardTypeRepository
                        .findById(cardTypeId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));
    }

    public CardTypeResponse createCardType(CardTypeRequest request) {
        CardType cardType = cardTypeMapper.toCardType(request);
        return cardTypeMapper.toCardTypeResponse(cardTypeRepository.save(cardType));
    }

    public CardTypeResponse updateCardType(Integer cardTypeId, CardTypeRequest request) {
        CardType cardType = cardTypeRepository
                .findById(cardTypeId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        cardTypeMapper.updateCardType(cardType, request);
        return cardTypeMapper.toCardTypeResponse(cardTypeRepository.save(cardType));
    }

    public void deleteCardType(Integer cardTypeId) {
        cardTypeRepository.deleteById(cardTypeId);
    }
}
