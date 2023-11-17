package com.zb.store.service;

import static com.zb.type.ErrorCode.ALREADY_EXISTED_STORE;
import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;
import static com.zb.type.ErrorCode.USER_NOT_FOUND;

import com.zb.dto.store.StoreDto;
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
        storeRepository.findByStoreName(form.getStoreName())
                       .ifPresent(store -> {
                           throw new CustomException(ALREADY_EXISTED_STORE);
                       });

        // 현재 로그인한 사용자가 매니저인지 확인
        String currentUsername = SecurityUtil.getCurrentUsername();
        Manager manager = managerRepository.findByUsername(currentUsername)
                                           .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 상점 등록
        Store store = StoreDto.Request.toEntity(form);
        store.setManager(manager);
        storeRepository.save(store);
    }

    @Override
    public void updateStore(StoreDto.Request form) {

    }

    @Override
    public void deleteStore(Long storeId) {

    }

    /**
     * 전체 상점 조회
     *
     * @return 상점 목록
     */
    @Override
    public List<StoreDto.Response> getStores() {
        return storeRepository.findAll().stream()
                              .map(StoreDto.Info::fromEntity)
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
    public StoreDto.Response getStore(Long storeId) {
        return storeRepository.findById(storeId)
                              .map(StoreDto.Info::fromEntity)
                              .map(StoreDto.Response::new)
                              .orElseThrow(() -> new CustomException(NOT_EXISTED_STORE));
    }
}
