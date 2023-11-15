package com.store.repository;

import com.store.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

//    @EntityGraph(attributePaths = "authorities")
//    Optional<Customer> findOneWithAuthoritiesByUsername(String username);

    Optional<Customer> findByUsername(String username);
}
