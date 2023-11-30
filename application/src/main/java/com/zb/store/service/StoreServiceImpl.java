package com.zb.store.service;

import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;

import com.querydsl.core.Tuple;
import com.zb.dto.store.StoreDto.StoreRequest;
import com.zb.dto.store.StoreDto.StoreResponse;
import com.zb.entity.Manager;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.StoreRepository;
import com.zb.repository.queryDsl.StoreQueryRepository;
import com.zb.service.ManagerDomainService;
import com.zb.service.StoreDomainService;
import com.zb.util.SecurityUtil;
import com.zb.validator.StoreValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreServce {

    private final StoreRepository storeRepository;
    private final StoreQueryRepository storeQueryRepository;
    private final StoreDomainService storeDomainService;
    private final ManagerDomainService managerDomainService;
    private final StoreValidator storeValidator;

    private String loggedInUsername;


    /**
     * 상점 등록
     */
    @Override
    @Transactional
    public void registerStore(StoreRequest form) {
        // 이미 존재하는 상점인지 확인
        storeDomainService.checkExistStore(form.getStoreName());
        // 현재 로그인한 매니저 조회
        Manager manager = managerDomainService.getLoggedInManager();
        // 상점 등록
        Store store = Store.from(form, manager);
        store.validate(storeValidator);
        storeRepository.save(store);
    }


    /**
     * 상점 수정
     */
    @Override
    @Transactional
    @CacheEvict(value = "storeInfo", key = "'storeId:' + #storeId")
    public void updateStore(StoreRequest form, Long storeId) {
        // 상점 수정할 수 있는지 확인
        loggedInUsername = SecurityUtil.getCurrentUsername();
        storeDomainService.checkCanUpdateStore(storeId, loggedInUsername);
        // 상점 수정
        storeQueryRepository.updateStore(form, storeId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "storeInfo", key = "'storeId:' + #storeId")
    public void deleteStore(Long storeId) {
        // 상점 삭제할 수 있는지 확인
        loggedInUsername = SecurityUtil.getCurrentUsername();
        storeDomainService.checkCanUpdateStore(storeId, loggedInUsername);
        // 상점 삭제
        storeQueryRepository.deleteStore(storeId);
    }

    /**
     * 전체 상점 조회
     */
    @Override
    @Transactional(readOnly = true)
    public Slice<StoreResponse> getStores(Pageable pageable) {
        return storeQueryRepository.findAllWithAverageRating(pageable)
                                   .map(StoreServiceImpl::convertToStoreResponse);
    }

    /**
     * 상점 상세 조회
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "storeInfo", key = "'storeId:' + #storeId")
    public StoreResponse getStore(Long storeId) {
        return storeQueryRepository.findByIdWithAverageRating(storeId)
                                   .map(StoreServiceImpl::convertToStoreResponse)
                                   .orElseThrow(() -> new CustomException(NOT_EXISTED_STORE));
    }

    private static StoreResponse convertToStoreResponse(Tuple result) {
        Store store = result.get(0, Store.class);
        Double averageRating = result.get(1, Double.class);
        StoreResponse response = new StoreResponse(Store.to(store));
        response.getInfo().setRating(averageRating == null ? 0 : averageRating);
        return response;
    }
}
