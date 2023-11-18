package com.zb.store.service;

import static com.zb.type.ErrorCode.ALREADY_EXISTED_STORE;
import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;
import static com.zb.type.ErrorCode.NOT_OWNER_STORE;
import static com.zb.type.ErrorCode.USER_NOT_FOUND;

import com.zb.dto.store.StoreDto;
import com.zb.dto.store.StoreDto.Request;
import com.zb.entity.Manager;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.ManagerRepository;
import com.zb.repository.StoreRepository;
import com.zb.util.SecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreServce {

    private final StoreRepository storeRepository;
    private final ManagerRepository managerRepository;

    /**
     * 상점 등록
     *
     * @param form 상점 등록 정보
     */
    @Override
    @Transactional
    public void registerStore(StoreDto.Request form) {
        // 이미 존재하는 상점인지 확인
        checkStore(form);
        // 현재 로그인한 매니저 조회
        Manager manager = geLoggedInManager();
        // 상점 등록
        Store store = Store.from(form, manager);
        storeRepository.save(store);
    }


    /**
     * 상점 수정
     *
     * @param form 상점 수정 정보
     */
    @Override
    @Transactional
    public void updateStore(StoreDto.Request form, Long storeId) {
        // 상점 조회 (권한 확인)
        Store store = getStoreManagedByCurrentUser(storeId);
        // 상점 수정
        store.updateStore(form);
    }

    @Override
    @Transactional
    public void deleteStore(Long storeId) {
        // 상점 조회 (권한 확인)
        Store store = getStoreManagedByCurrentUser(storeId);
        // 상점 삭제
        storeRepository.delete(store);
    }

    /**
     * 전체 상점 조회
     *
     * @return 상점 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreDto.Response> getStores() {
        return storeRepository.findAll().stream()
                              .map(Store::to)
                              .map(StoreDto.Response::new)
                              .toList();
    }

    /**
     * 상점 상세 조회
     *
     * @param storeId 상점 아이디
     * @return 상점 상세 정보
     */
    @Override
    @Transactional(readOnly = true)
    public StoreDto.Response getStore(Long storeId) {
        return storeRepository.findById(storeId)
                              .map(Store::to)
                              .map(StoreDto.Response::new)
                              .orElseThrow(() -> new CustomException(NOT_EXISTED_STORE));
    }

    private void checkStore(Request form) {
        storeRepository.findByStoreName(form.getStoreName())
                       .ifPresent(store -> {
                           throw new CustomException(ALREADY_EXISTED_STORE);
                       });
    }

    private Store getStoreManagedByCurrentUser(Long storeId) {
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

    private Manager geLoggedInManager() {
        String currentUsername = SecurityUtil.getCurrentUsername();

        return managerRepository.findByUsername(currentUsername)
                                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
