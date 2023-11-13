package com.uexcel.spring.security.client.model;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ChangeEmailModel {
    private String oldEmail;
    private String newEmail;
    private String oldEmailtoken;
    private String newEmailtoken;
    private String password;
}
