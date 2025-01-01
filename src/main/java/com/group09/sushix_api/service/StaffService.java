package com.group09.sushix_api.service;

import com.group09.sushix_api.dto.request.StaffRequest;
import com.group09.sushix_api.dto.response.StaffResponse;
import com.group09.sushix_api.dto.response.WorkHistoryResponse;
import com.group09.sushix_api.entity.Account;
import com.group09.sushix_api.entity.Staff;
import com.group09.sushix_api.entity.StaffInfo;
import com.group09.sushix_api.exception.AppException;
import com.group09.sushix_api.exception.ErrorCode;
import com.group09.sushix_api.mapper.StaffMapper;
import com.group09.sushix_api.mapper.WorkHistoryMapper;
import com.group09.sushix_api.repository.AccountRepository;
import com.group09.sushix_api.repository.StaffInfoRepository;
import com.group09.sushix_api.repository.StaffRepository;
import com.group09.sushix_api.repository.WorkHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class StaffService {
    StaffRepository staffRepository;
    StaffInfoRepository staffInfoRepository;
    AccountRepository accountRepository;
    WorkHistoryRepository workHistoryRepository;
    StaffMapper staffMapper;
    WorkHistoryMapper workHistoryMapper;
    PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> fetchStaffs(Integer page, Integer limit, String searchTerm, Integer branchId, String department) {
        List<Staff> items = staffRepository.fetchStaffs(page, limit, searchTerm, branchId, department);
        Integer totalCount = staffRepository.fetchStaffsCount(searchTerm, branchId, department);

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("totalCount", totalCount);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public StaffResponse getStaff(Integer staffId) {
        return staffMapper.toStaffResponse(
                staffRepository
                        .findById(staffId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED)),
                staffInfoRepository
                        .findById(staffId)
                        .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED))
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public StaffResponse createStaff(StaffRequest request) {
        Staff staff = staffMapper.toStaff(request);
        staff.setIsBranchManager(Objects.equals(request.getDeptName(), "Manager"));
        staff = staffRepository.save(staff);

        StaffInfo staffInfo = staffMapper.toStaffInfo(request);
        staffInfo.setStaffId(staff.getStaffId());
        staffInfo = staffInfoRepository.save(staffInfo);

        String startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        staffRepository.transferStaff(
                staff.getStaffId(),
                request.getBranchId(),
                request.getDeptName(),
                startDate
        );

        staff = staffRepository
                .findById(staffInfo.getStaffId())
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        Account account = Account.builder()
                .username(request.getStaffPhoneNumber())
                .password(passwordEncoder.encode(request.getStaffCitizenId()))
                .customer(null).staff(staff).isAdmin(false)
                .build();
        accountRepository.save(account);

        return staffMapper.toStaffResponse(staff, staffInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    public StaffResponse updateStaff(Integer staffId, StaffRequest request) {
        Staff staff = staffRepository
                .findById(staffId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        StaffInfo staffInfo = staffInfoRepository
                .findById(staffId)
                .orElseThrow(() -> new AppException(ErrorCode.OBJECT_NOT_EXISTED));

        staffMapper.updateStaff(staff, request);
        staffMapper.updateStaffInfo(staffInfo, request);

        staff = staffRepository.save(staff);
        staffInfo = staffInfoRepository.save(staffInfo);
        String startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        staffRepository.transferStaff(
                staff.getStaffId(),
                request.getBranchId(),
                request.getDeptName(),
                startDate
        );
        return staffMapper.toStaffResponse(staff, staffInfo);
    }

    public void deleteStaff(Integer staffId) {
        staffRepository.deleteById(staffId);
    }

    public List<WorkHistoryResponse> getStaffWorkHistory(Integer staffId) {
        return workHistoryRepository
                .getStaffWorkHistory(staffId)
                .stream()
                .map(workHistoryMapper::toWorkHistoryResponse)
                .toList();
    }
}
