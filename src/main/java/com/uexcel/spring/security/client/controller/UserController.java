package com.uexcel.spring.security.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uexcel.spring.security.client.entity.User;
import com.uexcel.spring.security.client.event.RegistrationCompleteEvent;
import com.uexcel.spring.security.client.model.UserModel;
import com.uexcel.spring.security.client.service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/registration")
    public String registration(@RequestBody UserModel userModel) {
        if (userModel.getPassword().equals(userModel.getMatchingPassword())) {
            User user = userService.savaUser(userModel);
            publisher.publishEvent(new RegistrationCompleteEvent(user, "url"));
            return "Success";

        }
        return "Password does not match!!!";

    }

}
