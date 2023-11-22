package com.zb.store.service;

import com.zb.dto.store.StoreDto.StoreRequest;
import com.zb.dto.store.StoreDto.StoreResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StoreServce {

    void registerStore(StoreRequest storeDto);

    void updateStore(StoreRequest storeDto, Long storeId);

    void deleteStore(Long storeId);

    Slice<StoreResponse> getStores(Pageable pageable);

    StoreResponse getStore(Long storeId);

}
