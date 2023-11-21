package com.zb.review.service;

import static com.zb.type.ErrorCode.NOT_EXISTED_REVIEW;
import static com.zb.type.ErrorCode.NOT_RESERVATION_OWNER;
import static com.zb.type.ErrorCode.USER_NOT_FOUND;

import com.zb.dto.review.ReviewDto;
import com.zb.entity.Customer;
import com.zb.entity.Reservation;
import com.zb.entity.Review;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.CustomerRepository;
import com.zb.repository.ReviewRepository;
import com.zb.service.ReservationDomainService;
import com.zb.service.ReviewDomainService;
import com.zb.util.SecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;
    private final ReservationDomainService reservationDomainService;
    private final ReviewDomainService reviewDomainService;

    /**
     * 리뷰 작성
     *
     * @param reservationId 예약 ID
     * @param form          리뷰 정보
     */
    @Override
    @Transactional
    public void writeReview(Long reservationId, ReviewDto.Request form) {
        // 현재 로그인한 고객 조회
        Customer customer = getLoggedInCustomer();

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

    @Override
    @Transactional
    public void modifyReview(Long reviewId, ReviewDto.Request form) {
        // 자신의 리뷰인지 확인
        Review dbReview = reviewRepository.findById(reviewId)
                                          .filter(review -> review.getCustomer().getUsername()
                                                                  .equals(SecurityUtil.getCurrentUsername()))
                                          .orElseThrow(() -> new CustomException(NOT_RESERVATION_OWNER));

        // 리뷰 수정
        dbReview.modify(form);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        // 자신의 리뷰인지 확인
        Review dbReview = reviewRepository.findById(reviewId)
                                          .filter(review -> review.getCustomer().getUsername()
                                                                  .equals(SecurityUtil.getCurrentUsername()))
                                          .orElseThrow(() -> new CustomException(NOT_RESERVATION_OWNER));

        // 리뷰 삭제
        reviewRepository.delete(dbReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto.Response> getReviewList(Long storeId) {
        return reviewRepository.findAllByStoreId(storeId).stream()
                               .map(Review::to)
                               .map(ReviewDto.Response::new)
                               .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDto.Response getReviewByReviewId(Long reviewId) {
        return reviewRepository.findById(reviewId)
                               .map(Review::to)
                               .map(ReviewDto.Response::new)
                               .orElseThrow(() -> new CustomException(NOT_EXISTED_REVIEW));
    }

    private Customer getLoggedInCustomer() {
        String username = SecurityUtil.getCurrentUsername();
        return customerRepository.findByUsername(username)
                                 .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
