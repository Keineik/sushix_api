package com.group09.sushix_api.mapper;

import com.group09.sushix_api.dto.request.AccountUpdateRequest;
import com.group09.sushix_api.dto.request.RegisterRequest;
import com.group09.sushix_api.dto.response.AccountResponse;
import com.group09.sushix_api.entity.Account;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toAccountResponse(Account account);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "staff", ignore = true)
    Account toAccount(RegisterRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccount(@MappingTarget Account account, AccountUpdateRequest request);
}
