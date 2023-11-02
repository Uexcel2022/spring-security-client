package com.uexcel.spring.security.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uexcel.spring.security.client.entity.User;
import com.uexcel.spring.security.client.model.UserModel;
import com.uexcel.spring.security.client.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private User user;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User savaUser(UserModel userModel) {
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setEmail(userModel.getEmail());
        user.setRole("USER");
        userRepository.save(user);
        return user;
    }

}
