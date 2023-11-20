package com.zb.entity;

import static com.zb.type.ErrorCode.DONT_CHANGE_RESERVATION_STATUS;

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
        checkStatus(ReservationStatus.CANCELED, ReservationStatus.ARRIVED);
        this.status = ReservationStatus.CANCELED;
    }

    // accept
    public void accept() {
        checkStatus(ReservationStatus.CANCELED, ReservationStatus.ARRIVED, ReservationStatus.ACCEPTED);
        this.status = ReservationStatus.ACCEPTED;
    }

    // reject
    public void reject() {
        checkStatus(ReservationStatus.CANCELED, ReservationStatus.ARRIVED, ReservationStatus.REJECTED);
        this.status = ReservationStatus.REJECTED;
    }

    private void checkStatus(ReservationStatus... invalidStatuses) {
        for (ReservationStatus invalidStatus : invalidStatuses) {
            if (this.status == invalidStatus) {
                throw new CustomException(DONT_CHANGE_RESERVATION_STATUS);
            }
        }
    }
}
