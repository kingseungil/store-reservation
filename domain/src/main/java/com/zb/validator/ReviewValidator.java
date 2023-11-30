package com.zb.validator;

import com.zb.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewValidator {

    public void validate(final Review review) {
        // Request @Valid에서 검증하지 않는 복잡한 부분 검증
    }
}
