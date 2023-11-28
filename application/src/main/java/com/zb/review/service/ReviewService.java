package com.zb.review.service;

import com.zb.dto.review.ReviewDto.ReviewRequest;
import com.zb.dto.review.ReviewDto.ReviewResponse;
import com.zb.dto.review.ReviewDto.ReviewResponseList;

public interface ReviewService {

    void writeReview(Long storeId, Long reservationId, ReviewRequest form);

    void modifyReview(Long storeId, Long reviewId, ReviewRequest form);

    void deleteReview(Long storeId, Long reviewId);

    ReviewResponseList getReviewList(Long storeId);

    ReviewResponse getReviewByReviewId(Long reviewId);


}
