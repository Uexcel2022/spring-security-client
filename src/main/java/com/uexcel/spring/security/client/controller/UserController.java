package com.uexcel.spring.security.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/hello")
    public String hello() {
        return "Welcome to my first spring security application!!!";
    }

    @PostMapping("/register")
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

    @GetMapping("/verifyRegistration")
    public String TokenValidation(@RequestParam("token") String token) {

        String result = userService.validateVarificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User varified successfully";
        }
        return "Bad user";
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerifyRegistrationToken(@RequestParam("token") String token, HttpServletRequest request) {
        return userService.resentToken(token, applicationUrl(request));

    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath();
    }

}
