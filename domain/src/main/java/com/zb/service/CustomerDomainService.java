package com.zb.service;


import static com.zb.type.ErrorCode.USER_NOT_FOUND;

import com.zb.entity.Customer;
import com.zb.exception.CustomException;
import com.zb.repository.CustomerQueryRepository;
import com.zb.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDomainService {

    private final CustomerQueryRepository customerQueryRepository;

    /**
     * 현재 로그인한 고객 조회
     */
    public Customer getLoggedInCustomer() {
        String username = SecurityUtil.getCurrentUsername();
        return customerQueryRepository.findByUsername(username)
                                      .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
