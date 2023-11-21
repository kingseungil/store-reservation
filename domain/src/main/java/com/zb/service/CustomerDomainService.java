package com.zb.service;


import static com.zb.type.ErrorCode.USER_NOT_FOUND;

import com.zb.entity.Customer;
import com.zb.exception.CustomException;
import com.zb.repository.CustomerRepository;
import com.zb.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDomainService {

    private final CustomerRepository customerRepository;

    public Customer getLoggedInCustomer() {
        String username = SecurityUtil.getCurrentUsername();
        return customerRepository.findByUsername(username)
                                 .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
