package com.uexcel.springs.ecurity.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uexcel.springs.ecurity.client.entity.User;
import com.uexcel.springs.ecurity.client.model.UserModel;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    User user;

    @Override
    public User savaUser(UserModel userModel) {
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setPassword(userModel.getPassword());
    }

}
