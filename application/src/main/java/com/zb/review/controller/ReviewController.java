package com.zb.review.controller;

import com.zb.annotation.CustomerOrAdmin;
import com.zb.annotation.OnlyCustomer;
import com.zb.dto.review.ReviewDto.ReviewRequest;
import com.zb.dto.review.ReviewDto.ReviewResponse;
import com.zb.dto.review.ReviewDto.ReviewResponseList;
import com.zb.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@Tag(name = "REVIEW")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/list/{storeId}")
    @Operation(summary = "리뷰 목록 조회")
    public ResponseEntity<ReviewResponseList> getReview(
      @PathVariable Long storeId
    ) {
        return ResponseEntity.ok(reviewService.getReviewList(storeId));
    }

    @GetMapping("/detail/{reviewId}")
    @Operation(summary = "리뷰 상세 조회")
    public ResponseEntity<ReviewResponse> getReviewByReviewId(
      @PathVariable Long reviewId
    ) {
        return ResponseEntity.ok(reviewService.getReviewByReviewId(reviewId));
    }

    @PostMapping("/reservation/{storeId}/{reservationId}")
    @OnlyCustomer
    @Operation(summary = "리뷰 작성")
    public ResponseEntity<String> writeReview(
      @PathVariable Long storeId,
      @PathVariable Long reservationId,
      @Valid @RequestBody ReviewRequest form
    ) {
        reviewService.writeReview(storeId, reservationId, form);
        return ResponseEntity.ok("리뷰 작성 완료");
    }

    @PutMapping("/{storeId}/{reviewId}")
    @OnlyCustomer
    @Operation(summary = "리뷰 수정")
    public ResponseEntity<String> modifyReview(
      @PathVariable Long storeId,
      @PathVariable Long reviewId,
      @Valid @RequestBody ReviewRequest form
    ) {
        reviewService.modifyReview(storeId, reviewId, form);
        return ResponseEntity.ok("리뷰 수정 완료");
    }

    @DeleteMapping("/{storeId}/{reviewId}")
    @CustomerOrAdmin
    @Operation(summary = "리뷰 삭제")
    public ResponseEntity<String> deleteReview(
      @PathVariable Long storeId,
      @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(storeId, reviewId);
        return ResponseEntity.ok("리뷰 삭제 완료");
    }

}
