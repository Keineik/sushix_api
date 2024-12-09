package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.AuthenticationRequest;
import com.group09.sushix_api.dto.request.RegisterRequest;
import com.group09.sushix_api.dto.response.AccountResponse;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.AuthenticationResponse;
import com.group09.sushix_api.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/register")
    ApiResponse<AccountResponse> register(@RequestBody RegisterRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .result(authenticationService.register(request))
                .build();
    }

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        boolean result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(AuthenticationResponse.builder()
                        .isAuthenticated(result)
                        .build())
                .build();
    }
}
