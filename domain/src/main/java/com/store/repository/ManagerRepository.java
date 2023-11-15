package com.store.repository;

import com.store.entity.Manager;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByUsername(String username);
}
