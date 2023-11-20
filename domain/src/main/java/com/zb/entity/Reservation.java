package com.zb.entity;

import static com.zb.type.ErrorCode.ALREADY_ARRIVED_RESERVATION;
import static com.zb.type.ErrorCode.ALREADY_CANCELED_RESERVATION;

import com.zb.dto.reservation.ReservationDto;
import com.zb.dto.user.CustomerDto;
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
    private ReservationStatus status = ReservationStatus.PENDING; // default

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
                          .status(ReservationStatus.PENDING) // default
                          .customer(customer)
                          .store(store)
                          .build();
    }

    // to
    public static ReservationDto.Info to(Reservation reservation) {
        return ReservationDto.Info.builder()
                                  .reservationDate(reservation.getReservationDate())
                                  .customer(CustomerDto.from(reservation.getCustomer().getUsername()))
                                  .storeName(reservation.getStore().getStoreName())
                                  .location(reservation.getStore().getLocation())
                                  .status(reservation.getStatus())
                                  .build();
    }

    // cancel
    public void cancel() {
        // 이미 취소된 예약인지 확인
        if (this.status == ReservationStatus.CANCELED) {
            throw new CustomException(ALREADY_CANCELED_RESERVATION);
        }

        // ARRIVED 상태인지 확인
        if (this.status == ReservationStatus.ARRIVED) {
            throw new CustomException(ALREADY_ARRIVED_RESERVATION);
        }

        this.status = ReservationStatus.CANCELED;
    }
}
