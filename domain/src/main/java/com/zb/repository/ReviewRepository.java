package com.zb.repository;

import com.zb.entity.Customer;
import com.zb.entity.Reservation;
import com.zb.entity.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select (count(r) > 0) from Review r where r.customer = :customer and r.reservation = :reservation")
    boolean existsByCustomerAndReservation(Customer customer, Reservation reservation);

    @Query("select r from Review r where r.store.storeId = :storeId order by r.createdAt DESC")
    @EntityGraph(attributePaths = {"customer", "store"})
    List<Review> findAllByStoreIdOrderByCreatedAtDesc(Long storeId);

    @EntityGraph(attributePaths = {"customer", "store"})
    @Override
    Optional<Review> findById(Long reviewId);

    @EntityGraph(attributePaths = {"customer", "store"})
    @Override
    void deleteById(Long reviewId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.store.storeId = :storeId")
    Optional<Double> findAverageRatingByStoreId(Long storeId);
}
