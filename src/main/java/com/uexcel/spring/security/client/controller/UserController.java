package com.uexcel.spring.security.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uexcel.spring.security.client.entity.ChangeEmail;
import com.uexcel.spring.security.client.entity.User;
import com.uexcel.spring.security.client.event.RegistrationCompleteEvent;
import com.uexcel.spring.security.client.model.ChangeEmailModel;
import com.uexcel.spring.security.client.model.UserModel;
import com.uexcel.spring.security.client.model.UserResetModel;
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
    public String register(@RequestBody UserModel userModel, final HttpServletRequest request) {
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

        return userService.validateVarificationToken(token);

    }

    @GetMapping("/resendVerificationToken")
    public String resendVerifyRegistrationToken(@RequestParam("token") String token, HttpServletRequest request) {
        return userService.resentToken(token, applicationUrl(request));

    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody UserResetModel userResetModel,
            HttpServletRequest request) {
        return userService.reset(userResetModel,
                applicationUrl(request), request.getServletPath());

    }

    @PostMapping("/resetPassword/{token}")
    public String resetPassword(@RequestBody UserResetModel resetModel,
            @PathVariable String token) {
        return userService.resetPassword(resetModel.getPassword(), token);

    }

    @PostMapping("/resendResetPasswordToken")
    public String ResendResetPasswordToken(@RequestBody UserResetModel resetModel, HttpServletRequest request) {
        return userService.reset(resetModel, applicationUrl(request),
                request.getServletPath());
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody UserResetModel resetModel, HttpServletRequest request) {
        return userService.reset(resetModel,
                applicationUrl(request), request.getServletPath());
    }

    @PutMapping("/changeName")
    public String updateProfile(@RequestBody UserResetModel resetModel, HttpServletRequest request) {
        return userService.reset(resetModel, null, request.getServletPath());

    }

    @PostMapping("/changeEmail")
    public String changeEmail(@RequestBody ChangeEmailModel changeEmailmModel,
            HttpServletRequest request) {
        return userService.resetEmail(changeEmailmModel, applicationUrl(request));

    }

    @GetMapping("/changeEmail")
    public String changeEmail(@RequestParam("token") String token, HttpServletRequest request) {
        return userService.EmailChangeValidation(token, applicationUrl(request));

    }

    @GetMapping("/saveNewEmail")
    public String saveNewEmail(
            @RequestParam("token") String token) {
        return userService.saveNewEmail(token);

    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath();
    }

}
