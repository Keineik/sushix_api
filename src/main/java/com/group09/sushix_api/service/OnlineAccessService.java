package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.OnlineAccessRequest;
import com.group09.sushix_api.dto.response.OnlineAccessResponse;
import com.group09.sushix_api.entity.OnlineAccess;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.OnlineAccessMapper;
import com.group09.sushix_api.repository.OnlineAccessRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OnlineAccessService {
    OnlineAccessRepository onlineAccessRepository;
    OnlineAccessMapper onlineAccessMapper;

    public OnlineAccessResponse getOnlineAccess(Integer custId) {
        return onlineAccessMapper.toOnlineAccessResponse(
                onlineAccessRepository
                        .findByCustomer_CustId(custId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED))
        );
    }

//    public OnlineAccessResponse createOnlineAccess(OnlineAccessRequest onlineAccessRequest) {
//
//    }

//    public OnlineAccessResponse updateOnlineAccess(Integer custId, OnlineAccessRequest onlineAccessRequest) {
//
//    }
}
