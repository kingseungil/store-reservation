package com.zb.store.service;

import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;

import com.zb.dto.store.StoreDto;
import com.zb.dto.store.StoreDto.Response;
import com.zb.entity.Manager;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.StoreRepository;
import com.zb.service.ManagerDomainService;
import com.zb.service.StoreDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreServce {

    private final StoreRepository storeRepository;
    private final StoreDomainService storeDomainService;
    private final ManagerDomainService managerDomainService;

    /**
     * 상점 등록
     * @param form 상점 등록 정보
     */
    @Override
    @Transactional
    public void registerStore(StoreDto.Request form) {
        // 이미 존재하는 상점인지 확인
        storeDomainService.checkExistStore(form.getStoreName());
        // 현재 로그인한 매니저 조회
        Manager manager = managerDomainService.getLoggedInManager();
        // 상점 등록
        Store store = Store.from(form, manager);
        storeRepository.save(store);
    }


    /**
     * 상점 수정
     * @param form 상점 수정 정보
     */
    @Override
    @Transactional
    public void updateStore(StoreDto.Request form, Long storeId) {
        // 상점 조회 (권한 확인)
        Store store = storeDomainService.getStoreManagedByCurrentUser(storeId);
        // 상점 수정
        store.updateStore(form);
    }

    @Override
    @Transactional
    public void deleteStore(Long storeId) {
        // 상점 조회 (권한 확인)
        Store store = storeDomainService.getStoreManagedByCurrentUser(storeId);
        // 상점 삭제
        storeRepository.delete(store);
    }

    /**
     * 전체 상점 조회
     * @return 상점 목록
     */
    @Override
    @Transactional(readOnly = true)
    public Slice<Response> getStores(Pageable pageable) {
        return storeRepository.findAllWithManager(pageable)
                              .map(Store::to)
                              .map(StoreDto.Response::new);
    }

    /**
     * 상점 상세 조회
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

}
