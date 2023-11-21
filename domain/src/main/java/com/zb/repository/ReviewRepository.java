package com.zb.repository;

import com.zb.entity.Customer;
import com.zb.entity.Reservation;
import com.zb.entity.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByCustomerAndReservation(Customer customer, Reservation reservation);

    @EntityGraph(attributePaths = {"customer", "store"})
    List<Review> findAllByStoreId(Long storeId);

    @EntityGraph(attributePaths = {"customer", "store"})
    @Override
    Optional<Review> findById(Long reviewId);
}
