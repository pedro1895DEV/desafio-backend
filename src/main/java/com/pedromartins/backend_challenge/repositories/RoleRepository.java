package com.pedromartins.backend_challenge.repositories;

import com.pedromartins.backend_challenge.models.Role;
import com.pedromartins.backend_challenge.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}

