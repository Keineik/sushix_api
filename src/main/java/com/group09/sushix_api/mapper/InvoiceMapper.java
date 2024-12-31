package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.response.InvoiceResponse;
import com.group09.sushix_api.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    @Mapping(target = "orderId", source = "order.orderId")
    InvoiceResponse toInvoiceResponse(Invoice invoice);
}
