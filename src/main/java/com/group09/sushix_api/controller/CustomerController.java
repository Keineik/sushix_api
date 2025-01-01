package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.request.CustomerRequest;
import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.CustomerResponse;
import com.group09.sushix_api.dto.response.OnlineAccessResponse;
import com.group09.sushix_api.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CustomerController {

    CustomerService customerService;

    @GetMapping
    ApiResponse<Map<String, Object>> fetchCustomers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "18") Integer limit,
            @RequestParam(defaultValue = "") String searchTerm
    ) {
        Map<String, Object> result = customerService.fetchCustomers(
                page,
                limit,
                searchTerm
        );

        return ApiResponse.<Map<String, Object>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/current")
    ApiResponse<CustomerResponse> getCurrentCustomer() {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.getCurrentCustomer())
                .build();
    }

    @GetMapping("/{customerId}")
    ApiResponse<CustomerResponse> getCustomer(@PathVariable("customerId") Integer customerId) {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.getCustomer(customerId))
                .build();
    }

    @GetMapping("/{custId}/online-access")
    ApiResponse<List<OnlineAccessResponse>> getCustomerOnlineAccess(@PathVariable("custId") Integer custId) {
        return ApiResponse.<List<OnlineAccessResponse>>builder()
                .result(customerService.getCustomerOnlineAccess(custId))
                .build();
    }

    @PostMapping("/online-access/{isStart}")
    ApiResponse<OnlineAccessResponse> logCustomerOnlineAccess(@PathVariable("isStart") Boolean isStart) {
        return ApiResponse.<OnlineAccessResponse>builder()
                .result(customerService.logCustomerOnlineAccess(isStart))
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
