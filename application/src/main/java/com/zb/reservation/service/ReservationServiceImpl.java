package com.zb.reservation.service;

import static com.zb.type.ErrorCode.ALREADY_EXISTED_RESERVATION;
import static com.zb.type.ErrorCode.NOT_EXISTED_RESERVATION;
import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;
import static com.zb.type.ErrorCode.NOT_OWNER_STORE;
import static com.zb.type.ErrorCode.NOT_RESERVATION_OWNER;
import static com.zb.type.ErrorCode.USER_NOT_FOUND;

import com.zb.dto.reservation.ReservationDto;
import com.zb.entity.Customer;
import com.zb.entity.Reservation;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.CustomerRepository;
import com.zb.repository.ReservationRepository;
import com.zb.repository.StoreRepository;
import com.zb.util.SecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    /* 손님용 */

    /**
     * 상점 예약
     *
     * @param storeId 상점 ID
     * @param form    예약 정보
     */
    @Override
    @Transactional
    public void reserve(Long storeId, ReservationDto.Request form) {
        // 현재 로그인한 고객 조회
        Customer customer = getLoggedInCustomer();
        // 상점 조회
        Store store = storeRepository.findById(storeId)
                                     .orElseThrow(() -> new CustomException(NOT_EXISTED_STORE));
        // 기간에 대한 유효성 검사
        List<Reservation> existReservation = reservationRepository.findByReservationDateAndStoreId(
          form.getReservationDate(), storeId);
        if (!existReservation.isEmpty()) {
            throw new CustomException(ALREADY_EXISTED_RESERVATION);
        }
        // 상점 예약
        Reservation reservation = Reservation.from(form, customer, store);
        reservationRepository.save(reservation);
    }

    /**
     * 예약 조회 (예약 ID로)
     *
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
     *
     * @param reservationId 예약 ID
     */
    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        // 자신의 예약인지 확인
        Reservation dbReservation = reservationRepository.findById(reservationId)
                                                         .filter(reservation -> reservation.getCustomer()
                                                                                           .getUsername()
                                                                                           .equals(
                                                                                             SecurityUtil.getCurrentUsername()))
                                                         .orElseThrow(() -> new CustomException(NOT_RESERVATION_OWNER));

        // 예약 취소
        dbReservation.cancel();
    }

    /**
     * 도착 처리
     *
     * @param reservationId 예약 ID
     */
    @Override
    @Transactional
    public void arriveReservation(Long reservationId) {
        // 자신의 예약인지 확인
        Reservation dbReseravtion = reservationRepository.findById(reservationId)
                                                         .filter(reservation -> reservation.getCustomer()
                                                                                           .getUsername()
                                                                                           .equals(
                                                                                             SecurityUtil.getCurrentUsername()))
                                                         .orElseThrow(() -> new CustomException(NOT_RESERVATION_OWNER));

        // 예약 도착 처리
        dbReseravtion.arrive();
    }

    /* 매니저용 */

    /**
     * 예약 조회 (상점별)
     *
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
     *
     * @param reservationId 예약 ID
     */
    @Override
    @Transactional
    public void acceptReservation(Long reservationId) {
        // 자기 매장 예약인지 확인
        Reservation dbReservation = getMyStoreReservation(reservationId);

        // 예약 상태 변경
        dbReservation.accept();
    }

    /**
     * 예약 거절
     *
     * @param reservationId 예약 ID
     */
    @Override
    @Transactional
    public void rejectReservation(Long reservationId) {
        // 자기 매장 예약인지 확인
        Reservation dbReservation = getMyStoreReservation(reservationId);

        // 예약 상태 변경
        dbReservation.reject();

    }


    private Reservation getMyStoreReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                                    .filter(reservation -> reservation.getStore()
                                                                      .getManager()
                                                                      .getUsername()
                                                                      .equals(
                                                                        SecurityUtil.getCurrentUsername()))
                                    .orElseThrow(() -> new CustomException(NOT_OWNER_STORE));
    }

    private Customer getLoggedInCustomer() {
        String username = SecurityUtil.getCurrentUsername();
        return customerRepository.findByUsername(username)
                                 .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

}
