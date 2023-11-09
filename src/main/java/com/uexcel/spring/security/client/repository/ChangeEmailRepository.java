package com.uexcel.spring.security.client.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uexcel.spring.security.client.entity.ChangeEmail;

@Repository
public interface ChangeEmailRepository extends JpaRepository<ChangeEmail, Long> {

    static Optional<ChangeEmail> findByToken(String token) {
        return null;
    }

    Optional<ChangeEmail> findByOldEmailToken(String token);

}
