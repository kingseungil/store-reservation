package com.zb.service;

import static com.zb.type.ErrorCode.ALREADY_WRITTEN_REVIEW;

import com.zb.entity.Customer;
import com.zb.entity.Reservation;
import com.zb.exception.CustomException;
import com.zb.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewDomainService {

    private final ReviewRepository reviewRepository;

    public void checkReviewExist(Customer customer, Reservation reservation) {
        if (reviewRepository.existsByCustomerAndReservation(customer, reservation)) {
            throw new CustomException(ALREADY_WRITTEN_REVIEW);
        }
    }

}
