package com.uexcel.spring.security.client.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uexcel.spring.security.client.entity.ChangeEmail;

@Repository
public interface ChangeEmailRepository extends JpaRepository<ChangeEmail, Long> {

    // Optional<ChangeEmail> findByTargetId(Long targetId);
    @Query(value = "SELECT * FROM change_email WHERE old_emailtoken=:token", nativeQuery = true)
    Optional<ChangeEmail> FindByOldEmailToken(@Param("token") String token);

    @Query(value = "SELECT * FROM change_email WHERE new_emailtoken=:token", nativeQuery = true)
    Optional<ChangeEmail> FindByNewEmailToken(@Param("token") String token);

}
