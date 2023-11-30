package com.zb.validator;

import com.zb.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationValidator {

    public void validate(final Reservation reservation) {
        // Request @Valid에서 검증하지 않는 복잡한 부분 검증
    }
}
