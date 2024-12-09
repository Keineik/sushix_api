package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.AccountResponse;
import com.group09.sushix_api.service.AccountService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountController {
    AccountService accountService;

//    @PostMapping
//    ApiResponse<AccountResponse> createAccount(@RequestBody @Valid CustomerRegisterRequest request) {
//        return ApiResponse.<AccountResponse>builder()
//                .result(accountService.createAccount(request))
//                .build();
//    }

    @GetMapping
    ApiResponse<List<AccountResponse>> getAccounts() {
        return ApiResponse.<List<AccountResponse>>builder()
                .result(accountService.getAccounts())
                .build();
    }

    @GetMapping("/{accountId}")
    ApiResponse<AccountResponse> getAccount(@PathVariable("accountId") Integer accountId) {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.getAccount(accountId))
                .build();
    }

//    @GetMapping("/my-info")
//    ApiResponse<AccountResponse> getMyInfo() {
//        return ApiResponse.<AccountResponse>builder()
//                .result(accountService.getMyInfo())
//                .build();
//    }

    @DeleteMapping("/{accountId}")
    ApiResponse<String> deleteAccount(@PathVariable Integer accountId) {
        accountService.deleteAccount(accountId);
        return ApiResponse.<String>builder().result("Account has been deleted").build();
    }

//    @PutMapping("/{accountId}")
//    ApiResponse<AccountResponse> updateAccount(@PathVariable Integer accountId, @RequestBody AccountUpdateRequest request) {
//        return ApiResponse.<AccountResponse>builder()
//                .result(accountService.updateAccount(accountId, request))
//                .build();
//    }
}
