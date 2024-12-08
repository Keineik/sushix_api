package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.CustomerRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.CustomerResponse;
import com.group09.sushix_api.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CustomerController {

    CustomerService customerService;

    @GetMapping
    ApiResponse<List<CustomerResponse>> getCustomers() {
        return ApiResponse.<List<CustomerResponse>>builder()
                .result(customerService.getAllCustomers())
                .build();
    }

    @GetMapping("/{customerId}")
    ApiResponse<CustomerResponse> getCustomer(@PathVariable("customerId") Integer customerId) {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.getCustomer(customerId))
                .build();
    }

    @PostMapping
    ApiResponse<CustomerResponse> createCustomer(@RequestBody @Valid CustomerRequest request) {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.createCustomer(request))
                .build();
    }

    @PutMapping("/{customerId}")
    ApiResponse<CustomerResponse> updateCustomer(@PathVariable("customerId") Integer customerId,
                                                 @RequestBody @Valid CustomerRequest request) {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.updateCustomer(customerId, request))
                .build();
    }

    @DeleteMapping("/{customerId}")
    ApiResponse<String> deleteCustomer(@PathVariable Integer customerId) {
        customerService.deleteCustomer(customerId);
        return ApiResponse.<String>builder().result("Customer has been deleted").build();
    }
}
