package com.tracer.repository;

import com.tracer.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByAuthority(String authority);
}
