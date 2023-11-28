package com.zb.reservation.service;

import static com.zb.type.ErrorCode.NOT_EXISTED_RESERVATION;
import static com.zb.type.ErrorCode.NOT_EXISTED_STORE;
import static com.zb.type.ReservationStatus.ACCEPTED;
import static com.zb.type.ReservationStatus.ARRIVED;
import static com.zb.type.ReservationStatus.CANCELED;
import static com.zb.type.ReservationStatus.REJECTED;

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
import com.zb.repository.queryDsl.ReservationQueryRepository;
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
    private final ReservationQueryRepository reservationQueryRepository;
    private final StoreRepository storeRepository;
    private final ReservationDomainService reservationDomainService;
    private final CustomerDomainService customerDomainService;
    private String loggedInUsername;

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
        // TODO : querydsl로 변경
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
        Reservation reservation = reservationQueryRepository.findById(reservationId)
                                                            .orElseThrow(
                                                              () -> new CustomException(NOT_EXISTED_RESERVATION));

        return ReservationResponse.from(reservation);
    }

    /**
     * 예약 취소
     */
    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        loggedInUsername = SecurityUtil.getCurrentUsername();
        // 취소할 수 있는지 확인
        reservationDomainService.checkStatusForCancel(reservationId);

        // 예약 취소
        reservationQueryRepository.updateReservationStatus(
          reservationId, loggedInUsername, CANCELED);
    }

    /**
     * 도착 처리
     */
    @Override
    @Transactional
    public void arriveReservation(Long reservationId) {
        loggedInUsername = SecurityUtil.getCurrentUsername();
        // 도착 처리할 수 있는지 확인
        reservationDomainService.checkStatusForArrive(reservationId);

        // 예약 도착 처리
        reservationQueryRepository.updateReservationStatus(
          reservationId, loggedInUsername, ARRIVED);
    }

    /* 매니저용 */

    /**
     * 예약 수락
     */
    @Override
    @Transactional
    public void acceptReservation(Long reservationId) {
        loggedInUsername = SecurityUtil.getCurrentUsername();
        // 예약 수락할 수 있는지 확인
        reservationDomainService.checkStatusForAccept(reservationId);

        // 예약 상태 변경
        reservationQueryRepository.updateReservationStatus(
          reservationId, loggedInUsername, ACCEPTED);
    }

    /**
     * 예약 거절
     */
    @Override
    @Transactional
    public void rejectReservation(Long reservationId) {
        loggedInUsername = SecurityUtil.getCurrentUsername();
        // 예약 거절할 수 있는지 확인
        reservationDomainService.checkStatusForReject(reservationId);

        // 예약 상태 변경
        reservationQueryRepository.updateReservationStatus(
          reservationId, loggedInUsername, REJECTED);
    }

    /**
     * 예약 조회 (상점별)
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationsResponse> getReservationsByStoreId(Long storeId) {
        List<Reservation> reservations = reservationQueryRepository.findByStoreIdOrderByReservationDateAsc(
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
                                                                   .status(reservation.getStatus())
                                                                   .info(Reservation.to(reservation))
                                                                   .build())
                           .sorted(Comparator.comparing(ReservationTimeTable::getTime))
                           .collect(Collectors.toList());
    }
}
