package com.group09.sushix_api.controller;

import com.group09.sushix_api.dto.response.ApiResponse;
import com.group09.sushix_api.dto.response.InvoiceResponse;
import com.group09.sushix_api.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvoiceController {
    InvoiceService invoiceService;

//    @GetMapping
//    ApiResponse<List<InvoiceResponse>> getInvoices() {
//        return ApiResponse.<List<InvoiceResponse>>builder()
//                .result(invoiceService.getAllInvoices())
//                .build();
//    }

    @GetMapping("/{invoiceId}")
    ApiResponse<InvoiceResponse> getInvoice(@PathVariable("invoiceId") Integer invoiceId) {
        return ApiResponse.<InvoiceResponse>builder()
                .result(invoiceService.getInvoice(invoiceId))
                .build();
    }
}
