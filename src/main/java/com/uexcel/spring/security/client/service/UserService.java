package com.uexcel.spring.security.client.service;

import com.uexcel.spring.security.client.entity.User;
import com.uexcel.spring.security.client.model.ChangeEmailModel;
import com.uexcel.spring.security.client.model.UserModel;
import com.uexcel.spring.security.client.model.UserResetModel;

public interface UserService {

    User savaUser(UserModel userModel);

    String saveUserVerificationToken(User user);

    String validateVarificationToken(String token);

    String resentToken(String token, String applicationUrl);

    String reset(UserResetModel userResetModel, String applicationUrl, String servletPath);

    String resetPassword(String password, String token);

    String EmailChangeValidation(String token, String string);

    String saveNewEmail(String token);

    String resetEmail(ChangeEmailModel changeEmailmModel, String applicationUrl);

}
