package com.zb.store.service;

import com.zb.dto.store.StoreDto;
import com.zb.dto.store.StoreDto.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StoreServce {

    void registerStore(StoreDto.Request storeDto);

    void updateStore(StoreDto.Request storeDto, Long storeId);

    void deleteStore(Long storeId);

    Slice<Response> getStores(Pageable pageable);

    StoreDto.Response getStore(Long storeId);

}
