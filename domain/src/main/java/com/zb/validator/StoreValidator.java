package com.zb.validator;

import com.zb.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreValidator {

    public void validate(final Store store) {
        // Request @Valid에서 검증하지 않는 복잡한 부분 검증
    }
}
