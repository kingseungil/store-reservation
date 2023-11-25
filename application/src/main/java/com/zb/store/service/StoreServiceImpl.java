package com.zb.store.service;

import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;

import com.zb.dto.store.StoreDto.StoreRequest;
import com.zb.dto.store.StoreDto.StoreResponse;
import com.zb.entity.Manager;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.ReviewRepository;
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
    private final ReviewRepository reviewRepository;
    private final StoreDomainService storeDomainService;
    private final ManagerDomainService managerDomainService;

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
        storeRepository.save(store);
    }


    /**
     * 상점 수정
     */
    @Override
    @Transactional
    public void updateStore(StoreRequest form, Long storeId) {
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
     */
    @Override
    @Transactional(readOnly = true)
    public Slice<StoreResponse> getStores(Pageable pageable) {
        return storeRepository.findAllWithAverageRating(pageable)
                              .map(result -> {
                                  Store store = (Store) result[0];
                                  Double averageRating = (Double) result[1];
                                  StoreResponse response = new StoreResponse(Store.to(store));
                                  response.getInfo().setRating(averageRating == null ? 0 : averageRating);
                                  return response;
                              });
    }

    /**
     * 상점 상세 조회
     */
    @Override
    @Transactional(readOnly = true)
    public StoreResponse getStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                                     .orElseThrow(() -> new CustomException(NOT_EXISTED_STORE));

        Double avergaRating = reviewRepository.findAverageRatingByStoreId(storeId).orElse(0.0);
        StoreResponse response = new StoreResponse(Store.to(store));
        response.getInfo().setRating(avergaRating);
        return response;
    }

}
