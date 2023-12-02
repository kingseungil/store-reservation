package com.zb.repository;

import com.zb.entity.Authority;
import com.zb.type.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, UserRole> {

}
