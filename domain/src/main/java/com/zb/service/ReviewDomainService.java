package com.zb.service;

import static com.zb.type.ErrorCode.ALREADY_WRITTEN_REVIEW;
import static com.zb.type.ErrorCode.NOT_EXISTED_REVIEW;
import static com.zb.type.ErrorCode.NOT_REVIEW_OWNER;

import com.zb.entity.Reservation;
import com.zb.entity.Review;
import com.zb.exception.CustomException;
import com.zb.repository.queryDsl.ReviewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewDomainService {

    private final ReviewQueryRepository reviewQueryRepository;

    /**
     * 리뷰 작성 여부 확인
     */
    public void checkReviewExist(Long customerId, Reservation reservation) {
        if (reviewQueryRepository.existsByCustomerAndReservation(customerId, reservation.getId())) {
            throw new CustomException(ALREADY_WRITTEN_REVIEW);
        }
    }

    /**
     * 리뷰 수정/삭제 가능한지 확인
     */
    public void checkCanUpdateReview(Long reviewId, String username) {
        Review review = reviewQueryRepository.findById(reviewId)
                                             .orElseThrow(() -> new CustomException(NOT_EXISTED_REVIEW));

        if (!review.getCustomer().getUsername().equals(username)) {
            throw new CustomException(NOT_REVIEW_OWNER);
        }
    }

}
