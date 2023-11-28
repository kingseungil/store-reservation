package com.zb.service;

import static com.zb.type.ErrorCode.ALREADY_EXISTED_STORE;
import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;
import static com.zb.type.ErrorCode.NOT_OWNER_STORE;

import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.ManagerRepository;
import com.zb.repository.StoreRepository;
import com.zb.repository.queryDsl.StoreQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreDomainService {

    private final StoreRepository storeRepository;
    private final StoreQueryRepository storeQueryRepository;
    private final ManagerRepository managerRepository;

    /**
     * 이미 존재하는 상점인지 확인
     */
    public void checkExistStore(String storeName) {
        if (storeQueryRepository.existsByStoreName(storeName)) {
            throw new CustomException(ALREADY_EXISTED_STORE);
        }
    }

    /**
     * 상점 수정/삭제할 수 있는지 확인
     */
    public void checkCanUpdateStore(Long storeId, String managerName) {
        Store store = storeQueryRepository.findById(storeId)
                                          .orElseThrow(() -> new CustomException(NOT_EXISTED_STORE));

        if (!store.getManager().getUsername().equals(managerName)) {
            throw new CustomException(NOT_OWNER_STORE);
        }
    }
}
