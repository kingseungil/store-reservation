package com.zb.reservation.service;

import static com.zb.type.ErrorCode.NOT_EXISTED_RESERVATION;
import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;
import static com.zb.type.ErrorCode.NOT_RESERVATION_OWNER;
import static com.zb.type.ErrorCode.USER_NOT_FOUND;

import com.zb.dto.reservation.ReservationDto;
import com.zb.entity.Customer;
import com.zb.entity.Reservation;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.CustomerRepository;
import com.zb.repository.ManagerRepository;
import com.zb.repository.ReservationRepository;
import com.zb.repository.StoreRepository;
import com.zb.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;
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

    /* 매니저용 */

    @Override
    public void getReservationByStoreId(Long storeId) {

    }


    @Override
    public void acceptReservation(Long reservationId) {

    }

    @Override
    public void rejectReservation(Long reservationId) {

    }

    private Customer getLoggedInCustomer() {
        String username = SecurityUtil.getCurrentUsername();
        return customerRepository.findByUsername(username)
                                 .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
