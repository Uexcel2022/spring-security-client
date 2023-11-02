package com.uexcel.spring.security.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.support.ServletContextApplicationContextInitializer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import com.uexcel.spring.security.client.entity.User;
import com.uexcel.spring.security.client.event.RegistrationCompleteEvent;
import com.uexcel.spring.security.client.model.UserModel;
import com.uexcel.spring.security.client.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired

    @PostMapping("/registration")
    public String registration(@RequestBody UserModel userModel, final HttpServletRequest request) {
        if (userModel.getPassword().equals(userModel.getMatchingPassword())) {
            User user = userService.savaUser(userModel);
            publisher.publishEvent(new RegistrationCompleteEvent(
                    user,
                    applicationUrl(request)));

            return "Success";

        }
        return "Password does not match!!!";

    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath();
    }

}
