package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.AccountUpdateRequest;
import com.group09.sushix_api.dto.response.AccountResponse;
import com.group09.sushix_api.entity.Account;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.AccountMapper;
import com.group09.sushix_api.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountService {
    AccountRepository accountRepository;
    AccountMapper accountMapper;

    public void deleteAccount(Integer accountId) {
        accountRepository.deleteById(accountId);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public List<AccountResponse> getAccounts() {
        log.info("In method get Accounts");
        return accountRepository.findAll().stream().map(accountMapper::toAccountResponse).toList();
    }

    public AccountResponse getAccount(Integer accountId) {
        return accountMapper.toAccountResponse(
                accountRepository
                        .findById(accountId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));
    }
}
