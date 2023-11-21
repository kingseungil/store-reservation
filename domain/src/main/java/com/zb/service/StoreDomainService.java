package com.zb.service;

import static com.zb.type.ErrorCode.ALREADY_EXISTED_STORE;
import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;
import static com.zb.type.ErrorCode.NOT_OWNER_STORE;
import static com.zb.type.ErrorCode.USER_NOT_FOUND;

import com.zb.entity.Manager;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.ManagerRepository;
import com.zb.repository.StoreRepository;
import com.zb.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreDomainService {

    private final StoreRepository storeRepository;
    private final ManagerRepository managerRepository;

    /**
     * 이미 존재하는 상점인지 확인
     * @param storeName 상점 이름
     */
    public void checkExistStore(String storeName) {
        storeRepository.findByStoreName(storeName)
                       .ifPresent(store -> {
                           throw new CustomException(ALREADY_EXISTED_STORE);
                       });
    }

    /**
     * 현재 로그인한 사용자가 관리하는 상점 조회
     * @param storeId 상점 ID
     * @return 상점 엔티티
     */
    public Store getStoreManagedByCurrentUser(Long storeId) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        Manager manager = managerRepository.findByUsername(currentUsername)
                                           .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                                     .orElseThrow(() -> new CustomException(NOT_EXISTED_STORE));

        if (!store.getManager().equals(manager)) {
            throw new CustomException(NOT_OWNER_STORE);
        }

        return store;
    }
}
