package com.uexcel.spring.security.client.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uexcel.spring.security.client.entity.ResetToken;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    @Query(value = "SELECT*FROM reset_token WHERE user_id=:userId", nativeQuery = true)
    Optional<ResetToken> findByUserId(@Param("userId") Long userId);

    Optional<ResetToken> findByToken(String token);

}
