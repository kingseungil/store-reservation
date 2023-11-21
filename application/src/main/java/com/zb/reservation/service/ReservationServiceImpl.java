package com.zb.reservation.service;

import static com.zb.type.ErrorCode.NOT_EXISTED_RESERVATION;
import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;

import com.zb.dto.reservation.ReservationDto;
import com.zb.entity.Customer;
import com.zb.entity.Reservation;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.ReservationRepository;
import com.zb.repository.StoreRepository;
import com.zb.service.CustomerDomainService;
import com.zb.service.ReservationDomainService;
import com.zb.util.SecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final ReservationDomainService reservationDomainService;
    private final CustomerDomainService customerDomainService;

    /* 손님용 */

    /**
     * 상점 예약
     * @param storeId 상점 ID
     * @param form    예약 정보
     */
    @Override
    @Transactional
    public void reserve(Long storeId, ReservationDto.Request form) {
        // 현재 로그인한 고객 조회
        Customer customer = customerDomainService.getLoggedInCustomer();
        // 상점 조회
        Store store = storeRepository.findById(storeId)
                                     .orElseThrow(() -> new CustomException(NOT_EXISTED_STORE));
        // 이미 예약이 있는지 확인
        reservationDomainService.checkExistReservation(storeId, form.getReservationDate());

        // 상점 예약
        Reservation reservation = Reservation.from(form, customer, store);
        reservationRepository.save(reservation);
    }

    /**
     * 예약 조회 (예약 ID로)
     * @param reservationId 예약 ID
     * @return 예약 정보
     */
    @Override
    @Transactional(readOnly = true)
    public ReservationDto.Response getReservationByReservationId(Long reservationId) {
        return reservationRepository.findById(reservationId)
                                    .map(Reservation::to)
                                    .map(ReservationDto.Response::new)
                                    .orElseThrow(() -> new CustomException(NOT_EXISTED_RESERVATION));
    }

    /**
     * 예약 취소
     * @param reservationId 예약 ID
     */
    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        // 자신의 예약인지 확인
        Reservation dbReservation = reservationDomainService.getReservationOfCustomer(reservationId,
          SecurityUtil.getCurrentUsername());

        // 예약 취소
        dbReservation.cancel();
    }

    /**
     * 도착 처리
     * @param reservationId 예약 ID
     */
    @Override
    @Transactional
    public void arriveReservation(Long reservationId) {
        // 자신의 예약인지 확인
        Reservation dbReservation = reservationDomainService.getReservationOfCustomer(reservationId,
          SecurityUtil.getCurrentUsername());

        // 예약 도착 처리
        dbReservation.arrive();
    }

    /* 매니저용 */

    /**
     * 예약 조회 (상점별)
     * @param storeId 상점 ID
     * @return 예약 목록 List
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto.Response> getReservationsByStoreId(Long storeId) {
        return reservationRepository.findAllByStoreId(storeId).stream()
                                    .map(Reservation::to)
                                    .map(ReservationDto.Response::new)
                                    .toList();
    }


    /**
     * 예약 수락
     * @param reservationId 예약 ID
     */
    @Override
    @Transactional
    public void acceptReservation(Long reservationId) {
        // 자기 매장 예약인지 확인
        Reservation dbReservation = reservationDomainService.getStoreOfManager(reservationId,
          SecurityUtil.getCurrentUsername());

        // 예약 상태 변경
        dbReservation.accept();
    }

    /**
     * 예약 거절
     * @param reservationId 예약 ID
     */
    @Override
    @Transactional
    public void rejectReservation(Long reservationId) {
        // 자기 매장 예약인지 확인
        Reservation dbReservation = reservationDomainService.getStoreOfManager(reservationId,
          SecurityUtil.getCurrentUsername());

        // 예약 상태 변경
        dbReservation.reject();

    }

}
