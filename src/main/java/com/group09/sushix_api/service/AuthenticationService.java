package com.group09.sushix_api.service;


import com.group09.sushix_api.dto.request.AuthenticationRequest;
import com.group09.sushix_api.dto.request.IntrospectRequest;
import com.group09.sushix_api.dto.request.RegisterRequest;
import com.group09.sushix_api.dto.response.AccountResponse;
import com.group09.sushix_api.dto.response.AuthenticationResponse;
import com.group09.sushix_api.dto.response.IntrospectResponse;
import com.group09.sushix_api.entity.Account;
import com.group09.sushix_api.entity.Customer;
import com.group09.sushix_api.entity.MembershipCard;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.AccountMapper;
import com.group09.sushix_api.repository.AccountRepository;
import com.group09.sushix_api.repository.CustomerRepository;
import com.group09.sushix_api.repository.MembershipCardRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    PasswordEncoder passwordEncoder;
    AccountRepository accountRepository;
    AccountMapper accountMapper;
    CustomerRepository customerRepository;
    MembershipCardRepository membershipCardRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Transactional
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

        Account account = accountMapper.toAccount(request);
        account.setCustomer(customer);
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setIsAdmin(false);

        customerRepository.save(customer);
        account = accountRepository.save(account);

        return accountMapper.toAccountResponse(account);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var account = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());

        if (!isAuthenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(account);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .isValid(verified && expiryTime.after(new Date()))
                .build();
    }

    private String generateToken(Account account) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(account.getAccountId()))
                .issuer("sushix.group09")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(30, ChronoUnit.DAYS).toEpochMilli()))
                .claim("scope", buildScope(account))
                .claim("accountId", account.getAccountId())
                .claim("username", account.getUsername())
                .claim("isAdmin", account.getIsAdmin())
                .claim("customer", account.getCustomer())
                .claim("staff", account.getStaff())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException exception) {
            log.error("Cannot create token", exception);
            throw new RuntimeException(exception);
        }
    }

    private String buildScope(Account account) {
        return ((account.getIsAdmin()) ? "ADMIN" : "")
                + ((account.getCustomer() != null) ? "CUSTOMER" : "")
                + ((account.getStaff() != null) ? "STAFF" : "")
                + ((account.getStaff() != null && Objects.equals(account.getStaff().getDepartment().getDeptName(), "Manager")) ? "MANAGER" : "");
    }
}
