package com.uexcel.spring.security.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uexcel.spring.security.client.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
