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
    @Query("select s from Store s")
    Slice<Store> findAllWithManager(Pageable pageable);

}
