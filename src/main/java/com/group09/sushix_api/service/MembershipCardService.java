package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.MembershipCardRequest;
import com.group09.sushix_api.dto.response.MembershipCardResponse;
import com.group09.sushix_api.entity.Account;
import com.group09.sushix_api.entity.CardType;
import com.group09.sushix_api.entity.Customer;
import com.group09.sushix_api.entity.MembershipCard;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.MembershipCardMapper;
import com.group09.sushix_api.repository.AccountRepository;
import com.group09.sushix_api.repository.CardTypeRepository;
import com.group09.sushix_api.repository.CustomerRepository;
import com.group09.sushix_api.repository.MembershipCardRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MembershipCardService {
    MembershipCardRepository membershipCardRepository;
    MembershipCardMapper membershipCardMapper;
    CustomerRepository customerRepository;
    CardTypeRepository cardTypeRepository;
    AccountRepository accountRepository;

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

    public MembershipCardResponse getMembershipCardByCustomerId(Integer custId) {
        MembershipCard membershipCard = membershipCardRepository
                .findByCustomer_CustId(custId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));
        return membershipCardMapper.toMembershipCardResponse(membershipCard);
    }

    public MembershipCardResponse createMembershipCard(MembershipCardRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.equals(auth.getName(), "anonymousUser"))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        Integer accountId = Integer.valueOf(auth.getName());
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        var staff = account.getStaff();
        if (staff == null)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        Customer customer = customerRepository
                .findById(request.getCustId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        MembershipCard membershipCard = membershipCardMapper.toMembershipCard(request);
        membershipCard.setCustomer(customer);
        membershipCard.setStaff(staff);

        return membershipCardMapper.toMembershipCardResponse(membershipCardRepository.save(membershipCard));
    }

    public void deleteMembershipCard(Integer cardId) {
        membershipCardRepository.deleteById(cardId);
    }
}
