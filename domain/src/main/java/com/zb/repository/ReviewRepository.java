package com.zb.repository;

import com.zb.entity.Customer;
import com.zb.entity.Reservation;
import com.zb.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByCustomerAndReservation(Customer customer, Reservation reservation);

}
