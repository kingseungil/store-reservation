package com.zb.repository;

import com.zb.entity.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"customer", "store"})
    List<Reservation> findAllByStoreId(Long storeId);

}
