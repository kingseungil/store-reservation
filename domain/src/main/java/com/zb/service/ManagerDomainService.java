package com.zb.service;


import static com.zb.type.ErrorCode.USER_NOT_FOUND;

import com.zb.entity.Manager;
import com.zb.exception.CustomException;
import com.zb.repository.ManagerRepository;
import com.zb.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerDomainService {

    private final ManagerRepository managerRepository;

    /**
     * 현재 로그인한 매니저 조회
     * @return 현재 로그인한 매니저
     */
    public Manager getLoggedInManager() {
        String currentUsername = SecurityUtil.getCurrentUsername();

        return managerRepository.findByUsername(currentUsername)
                                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
