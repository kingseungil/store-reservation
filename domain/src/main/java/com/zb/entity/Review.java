package com.zb.entity;

import com.zb.dto.review.ReviewDto;
import com.zb.dto.review.ReviewDto.ReviewRequest;
import com.zb.dto.store.StoreInfoDto;
import com.zb.dto.user.CustomerInfoDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "rating", nullable = false)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    // from
    public static Review from(ReviewRequest form, Customer customer, Store store, Reservation reservation) {
        return Review.builder()
                     .content(form.getContent())
                     .rating(form.getRating())
                     .customer(customer)
                     .store(store)
                     .reservation(reservation)
                     .build();
    }

    // to
    public static ReviewDto.Info to(Review review) {
        return ReviewDto.Info.builder()
                             .id(review.getId())
                             .content(review.getContent())
                             .rating(review.getRating())
                             .createdAt(review.getCreatedAt())
                             .customer(CustomerInfoDto.from(review.getCustomer()))
                             .store(StoreInfoDto.from(review.getStore()))
                             .build();
    }

}
