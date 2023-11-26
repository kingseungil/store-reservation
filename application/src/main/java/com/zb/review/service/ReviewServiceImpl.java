package com.zb.review.service;

import static com.zb.type.ErrorCode.NOT_EXISTED_REVIEW;
import static com.zb.type.UserRole.ROLE_ADMIN;

import com.zb.dto.review.ReviewDto.ReviewRequest;
import com.zb.dto.review.ReviewDto.ReviewResponse;
import com.zb.entity.Customer;
import com.zb.entity.Reservation;
import com.zb.entity.Review;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.ReviewRepository;
import com.zb.service.CustomerDomainService;
import com.zb.service.ReservationDomainService;
import com.zb.service.ReviewDomainService;
import com.zb.util.SecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationDomainService reservationDomainService;
    private final ReviewDomainService reviewDomainService;
    private final CustomerDomainService customerDomainService;

    /**
     * 리뷰 작성
     */
    @Override
    @Transactional
    @CacheEvict(value = "reviewList", key = "'storeId:' + #storeId")
    public void writeReview(Long storeId, Long reservationId, ReviewRequest form) {
        // 현재 로그인한 고객 조회
        Customer customer = customerDomainService.getLoggedInCustomer();

        // 예약 조회 및 상태 확인
        Reservation reservation = reservationDomainService.getReservationForReivew(reservationId,
          customer.getCustomerId());

        // 리뷰 작성 여부 확인
        reviewDomainService.checkReviewExist(customer, reservation);

        // 해당 상점 조회
        Store store = reservation.getStore();

        // 리뷰 작성
        Review review = Review.from(form, customer, store, reservation);
        reviewRepository.save(review);
    }

    /**
     * 리뷰 수정
     */
    @Override
    @Transactional
    @CacheEvict(value = "reviewList", key = "'storeId:' + #storeId")
    public void modifyReview(Long storeId, Long reviewId, ReviewRequest form) {
        // 자신의 리뷰인지 확인
        Review dbReview = reviewDomainService.getReviewOfCustomer(reviewId,
          SecurityUtil.getCurrentUsername());
        // 리뷰 수정
        dbReview.modify(form);
    }

    /**
     * 리뷰 삭제
     */
    @Override
    @Transactional
    @CacheEvict(value = "reviewList", key = "'storeId:' + #storeId")
    public void deleteReview(Long storeId, Long reviewId) {
        if (SecurityUtil.hasRole(ROLE_ADMIN)) {
            // 관리자인 경우 바로 리뷰 삭제
            reviewRepository.deleteById(reviewId);
        } else {
            // 자신의 리뷰인지 확인
            Review dbReview = reviewDomainService.getReviewOfCustomer(reviewId,
              SecurityUtil.getCurrentUsername());
            // 리뷰 삭제
            reviewRepository.delete(dbReview);
        }
    }

    /**
     * 상점 리뷰 조회
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "reviewList", key = "'storeId:' + #storeId")
    public List<ReviewResponse> getReviewList(Long storeId) {
        return reviewRepository.findAllByStoreId(storeId).stream()
                               .map(Review::to)
                               .map(ReviewResponse::new)
                               .toList();
    }

    /**
     * 리뷰 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReviewByReviewId(Long reviewId) {
        return reviewRepository.findById(reviewId)
                               .map(Review::to)
                               .map(ReviewResponse::new)
                               .orElseThrow(() -> new CustomException(NOT_EXISTED_REVIEW));
    }

}
