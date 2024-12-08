package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.MembershipCardRequest;
import com.group09.sushix_api.dto.response.MembershipCardResponse;
import com.group09.sushix_api.entity.CardType;
import com.group09.sushix_api.entity.Customer;
import com.group09.sushix_api.entity.MembershipCard;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.MembershipCardMapper;
import com.group09.sushix_api.repository.CardTypeRepository;
import com.group09.sushix_api.repository.CustomerRepository;
import com.group09.sushix_api.repository.MembershipCardRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MembershipCardService {
    MembershipCardRepository membershipCardRepository;
    MembershipCardMapper membershipCardMapper;
    CustomerRepository customerRepository;
    CardTypeRepository cardTypeRepository;

    public List<MembershipCardResponse> getAllMembershipCards() {
        return membershipCardRepository
                .findAll()
                .stream()
                .map(membershipCardMapper::toMembershipCardResponse)
                .toList();
    }

    public MembershipCardResponse getMembershipCard(Integer cardId) {
        return membershipCardMapper.toMembershipCardResponse(
                membershipCardRepository
                        .findById(cardId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));
    }

    public MembershipCardResponse createMembershipCard(MembershipCardRequest request) {
        // Check if the customer and card type exist
        Customer customer = customerRepository
                .findById(request.getCustId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));
        CardType cardType = cardTypeRepository
                .findById(request.getCardTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        // Create the MembershipCard entity
        MembershipCard membershipCard = membershipCardMapper.toMembershipCard(request);
        membershipCard.setCustomer(customer);
        membershipCard.setCardType(cardType);

        // Save and return the MembershipCardResponse
        return membershipCardMapper.toMembershipCardResponse(membershipCardRepository.save(membershipCard));
    }

    public MembershipCardResponse updateMembershipCard(Integer cardId, MembershipCardRequest request) {
        // Find the existing membership card
        MembershipCard membershipCard = membershipCardRepository
                .findById(cardId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        // Check if the customer and card type exist
        Customer customer = customerRepository
                .findById(request.getCustId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));
        CardType cardTypeId = cardTypeRepository
                .findById(request.getCardTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        // Update the entity
        membershipCardMapper.updateMembershipCard(membershipCard, request);
        membershipCard.setCustomer(customer);
        membershipCard.setCardType(cardTypeId);

        // Save and return the updated MembershipCardResponse
        return membershipCardMapper.toMembershipCardResponse(membershipCardRepository.save(membershipCard));
    }

    public void deleteMembershipCard(Integer cardId) {
        membershipCardRepository.deleteById(cardId);
    }
}
