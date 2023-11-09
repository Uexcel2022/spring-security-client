package com.uexcel.spring.security.client.model;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class UserResetModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String oldPassword;
    private String newPassword;
    private String matchingPassword;
    private String newEmail;
}
