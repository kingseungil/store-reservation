package com.zb.review.controller;

import com.zb.annotation.CustomerOrAdmin;
import com.zb.annotation.OnlyCustomer;
import com.zb.dto.review.ReviewDto;
import com.zb.review.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/list/{storeId}")
    public ResponseEntity<List<ReviewDto.Response>> getReview(
      @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(reviewService.getReviewList(storeId));
    }

    @GetMapping("{reviewId}")
    public ResponseEntity<ReviewDto.Response> getReviewByReviewId(
      @PathVariable Long reviewId
    ) {
        return ResponseEntity.ok(reviewService.getReviewByReviewId(reviewId));
    }

    @PostMapping("{reservationId}")
    @OnlyCustomer
    public ResponseEntity<String> writeReview(
      @PathVariable Long reservationId,
      @Valid @RequestBody ReviewDto.Request form
    ) {
        reviewService.writeReview(reservationId, form);
        return ResponseEntity.ok("리뷰 작성 완료");
    }

    @PutMapping("{reviewId}")
    @OnlyCustomer
    public ResponseEntity<String> modifyReview(
      @PathVariable Long reviewId,
      @Valid @RequestBody ReviewDto.Request form
    ) {
        reviewService.modifyReview(reviewId, form);
        return ResponseEntity.ok("리뷰 수정 완료");
    }

    @DeleteMapping("{reviewId}")
    @CustomerOrAdmin
    public ResponseEntity<String> deleteReview(
      @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("리뷰 삭제 완료");
    }

}
