package com.zb.review.service;

import com.zb.dto.review.ReviewDto;
import java.util.List;

public interface ReviewService {

    void writeReview(Long reservationId, ReviewDto.Request form);

    void modifyReview(Long reviewId, ReviewDto.Request form);

    void deleteReview(Long reviewId);

    List<ReviewDto.Response> getReviewList(Long storeId);

    ReviewDto.Response getReviewByReviewId(Long reviewId);


}
