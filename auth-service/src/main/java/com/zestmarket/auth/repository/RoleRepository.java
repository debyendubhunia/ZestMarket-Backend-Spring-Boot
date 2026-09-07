package com.zestmarket.auth.repository;

import com.zestmarket.auth.entity.ERole;
import com.zestmarket.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
