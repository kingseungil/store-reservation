package com.zb.repository;

import com.zb.entity.Store;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByStoreName(String storeName);

    @EntityGraph(attributePaths = {"manager"})
    @Query("SELECT s, AVG(r.rating) as avgRating FROM Store s LEFT JOIN s.reviews r GROUP BY s.id ORDER BY avgRating DESC")
    Slice<Object[]> findAllWithAverageRating(Pageable pageable);

}
