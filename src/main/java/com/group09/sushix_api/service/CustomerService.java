package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.CustomerRequest;
import com.group09.sushix_api.dto.response.CustomerResponse;
import com.group09.sushix_api.dto.response.MenuItemResponse;
import com.group09.sushix_api.dto.response.OnlineAccessResponse;
import com.group09.sushix_api.entity.Account;
import com.group09.sushix_api.entity.Customer;
import com.group09.sushix_api.entity.MenuItem;
import com.group09.sushix_api.entity.OnlineAccess;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.CustomerMapper;
import com.group09.sushix_api.mapper.OnlineAccessMapper;
import com.group09.sushix_api.repository.AccountRepository;
import com.group09.sushix_api.repository.CustomerRepository;
import com.group09.sushix_api.repository.OnlineAccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    AccountRepository accountRepository;
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    OnlineAccessRepository onlineAccessRepository;
    OnlineAccessMapper onlineAccessMapper;

    //@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN, SCOPE_STAFF, SCOPE_MANAGER')")
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository
                .findAll()
                .stream()
                .map(customerMapper::toCustomerResponse)
                .toList();
    }

    @Transactional
    public Map<String, Object> fetchCustomers(
            Integer page,
            Integer limit,
            String searchTerm
    ) {
        List<Customer> customers = customerRepository.fetchCustomers(
                page,
                limit,
                searchTerm
        );

        Integer totalCount = customerRepository.countCustomers(
                searchTerm
        );
        List<CustomerResponse> customerResponses = customers.stream()
                .map(customerMapper::toCustomerResponse)
                .toList();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("items", customerResponses);
        responseMap.put("totalCount", totalCount);

        return responseMap;
    }

    public CustomerResponse getCurrentCustomer() {
        Account account = null;
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            Integer accountId = Integer.valueOf(auth.getName());
            account = accountRepository
                    .findById(accountId)
                    .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));
        } catch (NumberFormatException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return customerMapper.toCustomerResponse(
                customerRepository
                        .findById(account.getCustomer().getCustId())
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));
    }

    public CustomerResponse getCustomer(Integer customerId) {
        return customerMapper.toCustomerResponse(
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)));
    }

    public List<OnlineAccessResponse> getCustomerOnlineAccess(Integer custId) {
        return onlineAccessRepository
                .findAllByCustId(custId)
                .stream()
                .map(onlineAccessMapper::toOnlineAccessResponse)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public OnlineAccessResponse logCustomerOnlineAccess(Boolean isStart) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.equals(auth.getName(), "anonymousUser"))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        Integer accountId = Integer.valueOf(auth.getName());
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));
        Customer customer = account.getCustomer();

        Integer accessId = onlineAccessRepository.logCustomerOnlineAccess(
                customer.getCustId(),
                isStart
        );

        OnlineAccess onlineAccess = onlineAccessRepository
                .findById(accessId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        return onlineAccessMapper.toOnlineAccessResponse(onlineAccess);
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = customerMapper.toCustomer(request);

        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    public CustomerResponse updateCustomer(Integer customerId, CustomerRequest request) {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        customerMapper.updateCustomer(customer, request);

        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    public void deleteCustomer(Integer customerId) {
        customerRepository.deleteById(customerId);
    }
}
