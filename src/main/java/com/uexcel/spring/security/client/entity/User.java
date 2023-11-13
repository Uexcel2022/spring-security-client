package com.uexcel.spring.security.client.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(name = "email_unique", columnNames = "email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(length = 60)
    private String password;
    private String role;
    private boolean enabled = false;

}
