package com.zb.repository;

import com.zb.entity.Reservation;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"customer", "store"})
    @Query("SELECT r FROM Reservation r WHERE r.store.storeId = :storeId ORDER BY r.reservationDate ASC")
    List<Reservation> findByStoreIdOrderByReservationDateAsc(Long storeId);

    @Query("select (count(r) > 0) from Reservation r where r.reservationDate = :reservationDate and r.store.storeId = :storeId")
    boolean existsByReservationDateAndStoreId(LocalDateTime reservationDate, Long storeId);
}
