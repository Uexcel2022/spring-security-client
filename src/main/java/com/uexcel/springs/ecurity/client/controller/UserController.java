package com.uexcel.springs.ecurity.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uexcel.springs.ecurity.client.model.UserModel;
import com.uexcel.springs.ecurity.client.service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/registration")
    public void registration(@RequestBody UserModel userModel) {
        if (userModel.getPassword().equals(userModel.getMatchingPassword())) {
            userService.savaUser(userModel);
        }

    }

}
