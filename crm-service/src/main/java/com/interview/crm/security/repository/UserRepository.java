package com.interview.crm.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interview.crm.security.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}