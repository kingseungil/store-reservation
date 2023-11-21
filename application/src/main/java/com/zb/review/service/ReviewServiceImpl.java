package com.zb.review.service;

import com.zb.dto.review.ReviewDto.Request;
import com.zb.dto.review.ReviewDto.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    @Override
    public void writeReview(Long reservationId, Request form) {
        
    }

    @Override
    public void modifyReview(Long reviewId, Request form) {

    }

    @Override
    public void deleteReview(Long reviewId) {

    }

    @Override
    public List<Response> getReviewList(Long storeId) {
        return null;
    }

    @Override
    public Response getReviewByReviewId(Long reviewId) {
        return null;
    }
}
