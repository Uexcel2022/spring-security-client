package com.uexcel.spring.security.client.service;

import com.uexcel.spring.security.client.entity.User;
import com.uexcel.spring.security.client.model.UserModel;

public interface UserService {

    User savaUser(UserModel userModel);

}
