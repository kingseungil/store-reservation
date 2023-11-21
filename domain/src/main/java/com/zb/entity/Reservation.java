package com.zb.entity;

import static com.zb.type.ErrorCode.DONT_CHANGE_RESERVATION_STATUS;
import static com.zb.type.ErrorCode.NOT_ARRIVE_TIME;
import static com.zb.type.ReservationStatus.ACCEPTED;
import static com.zb.type.ReservationStatus.ARRIVED;
import static com.zb.type.ReservationStatus.CANCELED;
import static com.zb.type.ReservationStatus.PENDING;
import static com.zb.type.ReservationStatus.REJECTED;

import com.zb.dto.reservation.ReservationDto;
import com.zb.dto.store.StoreInfoDto;
import com.zb.dto.user.CustomerInfoDto;
import com.zb.exception.CustomException;
import com.zb.type.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends BaseEntity {

    @Id
    @Column(name = "reservation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    // from
    public static Reservation from(ReservationDto.Request form, Customer customer, Store store) {
        return Reservation.builder()
                          .reservationDate(form.getReservationDate())
                          .status(PENDING) // default
                          .customer(customer)
                          .store(store)
                          .build();
    }

    // to
    public static ReservationDto.Info to(Reservation reservation) {
        return ReservationDto.Info.builder()
                                  .reservationDate(reservation.getReservationDate())
                                  .customer(CustomerInfoDto.from(reservation.getCustomer().getUsername()))
                                  .store(StoreInfoDto.from(reservation.getStore().getStoreName(),
                                    reservation.getStore().getLocation(),
                                    reservation.getStatus()))
                                  .build();
    }

    // cancel
    public void cancel() {
        checkStatus(CANCELED, ARRIVED);
        this.status = CANCELED;
    }

    // accept
    public void accept() {
        checkStatus(CANCELED, ARRIVED, ACCEPTED);
        this.status = ACCEPTED;
    }

    // reject
    public void reject() {
        checkStatus(CANCELED, ARRIVED, REJECTED);
        this.status = REJECTED;
    }

    // arrive
    public void arrive() {
        checkStatus(PENDING, CANCELED, REJECTED, ARRIVED);
        // 현재 시간과 예약 시간 비교
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(this.reservationDate.minusMinutes(10)) || now.isAfter(this.reservationDate)) {
            throw new CustomException(NOT_ARRIVE_TIME);
        }

        this.status = ARRIVED;
    }

    private void checkStatus(ReservationStatus... invalidStatuses) {
        for (ReservationStatus invalidStatus : invalidStatuses) {
            if (this.status == invalidStatus) {
                throw new CustomException(DONT_CHANGE_RESERVATION_STATUS);
            }
        }
    }
}
