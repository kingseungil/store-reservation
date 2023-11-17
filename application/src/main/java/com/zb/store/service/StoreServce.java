package com.zb.store.service;

import com.zb.dto.store.StoreDto;
import java.util.List;

public interface StoreServce {

    void registerStore(StoreDto storeDto);

    void updateStore(StoreDto storeDto);

    void deleteStore(Long storeId);

    List<StoreDto> getStores();

    StoreDto getStore(Long storeId);

}
