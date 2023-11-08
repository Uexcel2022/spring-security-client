package com.uexcel.spring.security.client.service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uexcel.spring.security.client.entity.User;
import com.uexcel.spring.security.client.entity.VerificationToken;
import com.uexcel.spring.security.client.model.UserModel;
import com.uexcel.spring.security.client.repository.UserRepository;
import com.uexcel.spring.security.client.repository.VerificationTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {
    User user = new User();
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    VerificationTokenRepository userTokenRepository;

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

    @Override
    public void saveUserVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        userTokenRepository.save(verificationToken);
    }

    public String validateVarificationToken(String token) {
        List<VerificationToken> usertoken = userTokenRepository.findByToken(token);

        if (usertoken.isEmpty()) {
            return "Bad user";
        }

        VerificationToken obj = usertoken.get(0);
        User user = obj.getUser();

        Calendar calendar = Calendar.getInstance();
        if (obj.getTakenExpirateTime().getTime() - calendar.getTime().getTime() <= 0) {
            userTokenRepository.delete(obj);
            return "Expired";
        }

        user.setEnabled(true);
        userRepository.save(user);

        return "valid";
    }

    @Override
    public String resentToken(String token, String applicationUrl) {
        List<VerificationToken> verificationToken = userTokenRepository.findByToken(token);
        if (verificationToken.isEmpty()) {
            return "Bad user";

        }

        VerificationToken oldToken = verificationToken.get(0);

        VerificationToken newToken = new VerificationToken(UUID.randomUUID().toString());

        oldToken.setToken(newToken.getToken());

        oldToken.setTakenExpirateTime(newToken.getTakenExpirateTime());

        userTokenRepository.save(oldToken);

        String url = applicationUrl +
                "/verifyRegistration?token=" + token;

        log.info("Click the link to verify your account: {}", url);

        return "Varification link has been sent to your email";
    }

}
