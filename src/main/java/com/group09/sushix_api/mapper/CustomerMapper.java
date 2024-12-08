package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.CustomerRequest;
import com.group09.sushix_api.dto.response.CustomerResponse;
import com.group09.sushix_api.entity.Customer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toCustomerResponse(Customer customer);

    Customer toCustomer(CustomerRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomer(@MappingTarget Customer customer, CustomerRequest request);
}
