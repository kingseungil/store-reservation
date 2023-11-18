package com.zb.store.service;

import com.zb.dto.store.StoreDto;
import java.util.List;

public interface StoreServce {

    void registerStore(StoreDto.Request storeDto);

    void updateStore(StoreDto.Request storeDto, Long storeId);

    void deleteStore(Long storeId);

    List<StoreDto.Response> getStores();

    StoreDto.Response getStore(Long storeId);

}
