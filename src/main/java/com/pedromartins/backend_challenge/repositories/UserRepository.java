package com.pedromartins.backend_challenge.repositories;

import com.pedromartins.backend_challenge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmails_Email(String email);
}

