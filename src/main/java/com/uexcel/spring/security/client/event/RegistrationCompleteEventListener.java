package com.uexcel.spring.security.client.entity.listener;

import java.util.UUID;

import org.springframework.context.ApplicationListener;

import com.uexcel.spring.security.client.entity.User;
import com.uexcel.spring.security.client.event.RegistrationCompleteEvent;

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // create verification token for user and send the varification link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
    }

}
