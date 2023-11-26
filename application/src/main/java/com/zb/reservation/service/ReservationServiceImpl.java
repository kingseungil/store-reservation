package com.zb.reservation.service;

import static com.zb.type.ErrorCode.NOT_EXISTED_RESERVATION;
import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;

import com.zb.dto.reservation.ReservationDto.ReservationRequest;
import com.zb.dto.reservation.ReservationDto.ReservationResponse;
import com.zb.dto.reservation.ReservationDto.ReservationTimeTable;
import com.zb.dto.reservation.ReservationDto.ReservationsResponse;
import com.zb.entity.Customer;
import com.zb.entity.Reservation;
import com.zb.entity.Store;
import com.zb.exception.CustomException;
import com.zb.repository.ReservationRepository;
import com.zb.repository.StoreRepository;
import com.zb.service.CustomerDomainService;
import com.zb.service.ReservationDomainService;
import com.zb.util.SecurityUtil;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
     */
    @Override
    @Transactional
    public void reserve(Long storeId, ReservationRequest form) {
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
     */
    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getReservationByReservationId(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                                                       .orElseThrow(() -> new CustomException(NOT_EXISTED_RESERVATION));

        return ReservationResponse.builder()
                                  .id(reservation.getId())
                                  .reservationDate(reservation.getReservationDate())
                                  .info(Reservation.to(reservation))
                                  .build();
    }

    /**
     * 예약 취소
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
     * 예약 수락
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

    /**
     * 예약 조회 (상점별)
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationsResponse> getReservationsByStoreId(Long storeId) {
        List<Reservation> reservations = reservationRepository.findByStoreIdOrderByReservationDateAsc(
          storeId);

        return groupReservationsByDate(reservations).entrySet().stream()
                                                    .map(this::createReservationsResponse)
                                                    .toList();
    }

    // 예약 날짜별로 그룹핑
    private Map<LocalDate, List<Reservation>> groupReservationsByDate(List<Reservation> reservations) {
        return reservations.stream()
                           .collect(
                             Collectors.groupingBy(reservation -> reservation.getReservationDate().toLocalDate()));
    }

    // 예약 날짜별로 응답 생성
    private ReservationsResponse createReservationsResponse(Map.Entry<LocalDate, List<Reservation>> entry) {
        List<ReservationTimeTable> timeTable = createTimeTable(entry.getValue());
        return ReservationsResponse.builder()
                                   .id(entry.getValue().get(0).getId())
                                   .reservationDate(entry.getKey().atStartOfDay())
                                   .timeTable(timeTable)
                                   .build();
    }


    // 예약 타임 테이블 생성
    private List<ReservationTimeTable> createTimeTable(List<Reservation> reservations) {
        return reservations.stream()
                           .map(reservation -> ReservationTimeTable.builder()
                                                                   .time(reservation.getReservationDate().toLocalTime())
                                                                   .info(Reservation.to(reservation))
                                                                   .build())
                           .sorted(Comparator.comparing(ReservationTimeTable::getTime))
                           .collect(Collectors.toList());
    }
}
