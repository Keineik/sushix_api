package com.group09.sushix_api.service;


import com.group09.sushix_api.dto.request.AuthenticationRequest;
import com.group09.sushix_api.dto.request.RegisterRequest;
import com.group09.sushix_api.dto.response.AccountResponse;
import com.group09.sushix_api.entity.Account;
import com.group09.sushix_api.entity.Customer;
import com.group09.sushix_api.entity.MembershipCard;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.AccountMapper;
import com.group09.sushix_api.repository.AccountRepository;
import com.group09.sushix_api.repository.CustomerRepository;
import com.group09.sushix_api.repository.MembershipCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    AccountRepository accountRepository;
    AccountMapper accountMapper;
    CustomerRepository customerRepository;
    MembershipCardRepository membershipCardRepository;


    public AccountResponse register(RegisterRequest request) {
        // Check if card exists
        boolean hasCard = false;
        if (request.getCardId() != null)
            hasCard = membershipCardRepository.existsById(request.getCardId());

        Customer customer;
        // If card exists
        if (hasCard) {
            MembershipCard membershipCard = membershipCardRepository
                    .findById(request.getCardId())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_CARD));

            Integer custId = membershipCard.getCardId();
            customer = customerRepository
                    .findById(custId)
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_CARD));
        } else {
            customer = Customer.builder()
                    .custName(request.getCustName())
                    .custEmail(request.getEmail())
                    .custPhoneNumber(request.getPhoneNumber())
                    .build();
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Account account = accountMapper.toAccount(request);
        account.setCustomer(customer);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setIsAdmin(false);

        customerRepository.save(customer);
        account = accountRepository.save(account);

        return accountMapper.toAccountResponse(account);
    }

    public boolean authenticate(AuthenticationRequest request) {
        var account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(request.getPassword(), account.getPassword());
    }
}
