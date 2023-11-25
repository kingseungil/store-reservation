package com.zb.service;

import static com.zb.type.ErrorCode.ALREADY_WRITTEN_REVIEW;
import static com.zb.type.ErrorCode.NOT_REVIEW_OWNER;

import com.zb.entity.Customer;
import com.zb.entity.Reservation;
import com.zb.entity.Review;
import com.zb.exception.CustomException;
import com.zb.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewDomainService {

    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 작성 여부 확인
     */
    public void checkReviewExist(Customer customer, Reservation reservation) {
        if (reviewRepository.existsByCustomerAndReservation(customer, reservation)) {
            throw new CustomException(ALREADY_WRITTEN_REVIEW);
        }
    }

    /**
     * 리뷰 작성자인지 확인
     */
    public Review getReviewOfCustomer(Long reviewId, String username) {
        return reviewRepository.findById(reviewId)
                               .filter(review -> review.getCustomer().getUsername().equals(username))
                               .orElseThrow(() -> new CustomException(NOT_REVIEW_OWNER));
    }
}
