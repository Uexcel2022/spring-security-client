package com.uexcel.spring.security.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uexcel.spring.security.client.entity.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @Query("SELECT s FROM VerificationToken s WHERE  s.token =:token")
    public VerificationToken findByToken(@Param("token") String token);
}
