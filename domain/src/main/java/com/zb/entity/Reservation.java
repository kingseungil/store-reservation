package com.zb.entity;

import com.zb.dto.reservation.ReservationDto;
import com.zb.type.ResevationStatus;
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
    private ResevationStatus status = ResevationStatus.PENDING; // default

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
                          .status(ResevationStatus.PENDING) // default
                          .customer(customer)
                          .store(store)
                          .build();
    }
}
