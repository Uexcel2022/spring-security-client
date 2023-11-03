package com.uexcel.spring.security.client.event;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.uexcel.spring.security.client.entity.User;
import com.uexcel.spring.security.client.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        // create verification token for user and send the varification link

        User user = event.getUser();

        String token = UUID.randomUUID().toString();

        userService.saveUserVerificationToken(user, token);

        // send email

        String url = event.getApplicationUrl() +
                "/verifyRegistration?token=" + token;

        // SendVerificationEmail
        log.info("Click the link to verify your account: {}", url);

    }

}
