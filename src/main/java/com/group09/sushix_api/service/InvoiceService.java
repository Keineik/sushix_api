package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.response.InvoiceResponse;
import com.group09.sushix_api.entity.Invoice;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.InvoiceMapper;
import com.group09.sushix_api.mapper.OrderDetailsMapper;
import com.group09.sushix_api.repository.AccountRepository;
import com.group09.sushix_api.repository.InvoiceRepository;
import com.group09.sushix_api.repository.OrderDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceService {
    InvoiceRepository invoiceRepository;
    OrderDetailsRepository orderDetailsRepository;
    InvoiceMapper invoiceMapper;
    OrderDetailsMapper orderDetailsMapper;

    @Transactional(rollbackFor = Exception.class)
    public InvoiceResponse getInvoice(Integer invoiceId) {
        Invoice invoice = invoiceRepository
                .findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        InvoiceResponse invoiceResponse = invoiceMapper.toInvoiceResponse(invoice);
        invoiceResponse.setOrderDetails(
                orderDetailsRepository
                        .findAllByOrderId(invoice.getOrder().getOrderId())
                        .stream()
                        .map(orderDetailsMapper::toOrderDetailsResponse)
                        .toList()
        );

        return invoiceResponse;
    }
}