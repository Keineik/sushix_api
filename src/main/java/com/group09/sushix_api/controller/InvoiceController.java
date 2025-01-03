package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.InvoiceResponse;
import com.group09.sushix_api.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvoiceController {
    InvoiceService invoiceService;

    @GetMapping
    ApiResponse<Map<String, Object>> fetchInvoices(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "18") Integer limit,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") Integer branchId,
            @RequestParam(defaultValue = "0") Integer custId,
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String endDate,
            @RequestParam(defaultValue = "false") Boolean sortDirection
    ) {
        Map<String, Object> result = invoiceService.fetchInvoices(
                page,
                limit,
                searchTerm,
                branchId,
                custId,
                startDate,
                endDate,
                sortDirection
        );
        return ApiResponse.<Map<String, Object>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{invoiceId}")
    ApiResponse<InvoiceResponse> getInvoice(@PathVariable("invoiceId") Integer invoiceId) {
        return ApiResponse.<InvoiceResponse>builder()
                .result(invoiceService.getInvoice(invoiceId))
                .build();
    }
}
