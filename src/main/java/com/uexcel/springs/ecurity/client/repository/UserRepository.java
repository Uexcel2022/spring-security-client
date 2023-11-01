package com.uexcel.springs.ecurity.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uexcel.springs.ecurity.client.entity.User;

@Repository
interface UserRepository extends JpaRepository<User, Long> {

}
