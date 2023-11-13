package com.uexcel.spring.security.client.service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uexcel.spring.security.client.entity.ChangeEmail;
import com.uexcel.spring.security.client.entity.ResetToken;
import com.uexcel.spring.security.client.entity.User;
import com.uexcel.spring.security.client.entity.VerificationToken;
import com.uexcel.spring.security.client.model.ChangeEmailModel;
import com.uexcel.spring.security.client.model.UserModel;
import com.uexcel.spring.security.client.model.UserResetModel;
import com.uexcel.spring.security.client.repository.ChangeEmailRepository;
import com.uexcel.spring.security.client.repository.ResetTokenRepository;
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

    @Autowired
    ResetTokenRepository resetTokenRepository;

    @Autowired
    ChangeEmailRepository changeEmailRepository;

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
    public String saveUserVerificationToken(User user) {
        VerificationToken verificationToken = new VerificationToken(user);
        userTokenRepository.save(verificationToken);
        ResetToken resetToken = new ResetToken(user);
        resetTokenRepository.save(resetToken);
        return verificationToken.getToken();
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

        return "You have been verified successfully";
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

    @Override
    public String reset(UserResetModel userResetModel, String applicationUrl, String servletPath) {
        User user = userRepository.findByEmail(userResetModel.getEmail());

        if (user == null) {
            return "In valid email";
        }
        Optional<ResetToken> resetToken = resetTokenRepository.findByUserId(user.getUserId());

        if (resetToken.isPresent() &&
                (servletPath.equals("/resetPassword") || servletPath.equals("/ResendResetPasswordToken"))) {
            ResetToken oldToken = resetToken.get();
            String url = applicationUrl + "/resetPassword/" + processToken(oldToken).getToken();
            log.info("Click on the link to reset password {}", url);

            return "Password reset link has been sent to your email";
        }

        if (servletPath.equals("/changePassword")) {
            boolean isValidPassword = validPassword(userResetModel, user);
            if (isValidPassword) {
                user.setPassword(passwordEncoder.encode(userResetModel.getNewPassword()));
                userRepository.save(user);
                // send mail your pass was changed to the user -----
                return "Your password has been changed successfully";
            }
        }

        if (servletPath.equals("/changeName")) {
            boolean isValidPassword = validPassword(userResetModel, user);
            if (isValidPassword) {
                if (userResetModel.getFirstName() != null) {
                    user.setFirstName(userResetModel.getFirstName());
                }
                if (userResetModel.getLastName() != null) {
                    user.setLastName(userResetModel.getLastName());
                }
                userRepository.save(user);

                return "Update successfull";
            }

        }

        return "Invalid email";
    }

    @Override
    public String resetPassword(String password, String token) {
        Optional<ResetToken> resetToken = resetTokenRepository.findByToken(token);
        if (resetToken.isPresent()) {
            Calendar calendar = Calendar.getInstance();
            if (resetToken.get().getExpirationTime().getTime() -
                    calendar.getTime().getTime() <= 0) {
                return "Expired";
            }

            User user = resetToken.get().getUser();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            ResetToken oldToken = resetToken.get();
            processToken(oldToken);
            return "Password has been reset successfully";
        }
        return "Bad Request";
    }

    @Override
    public String resetEmail(ChangeEmailModel changeEmailModel, String applicationUrl) {

        User user = userRepository.findByEmail(changeEmailModel.getOldEmail());
        if (user != null && changeEmailModel.getNewEmail() != null) {
            boolean isvalid = passwordEncoder.matches(changeEmailModel.getPassword(),
                    user.getPassword());
            if (!isvalid) {
                return "The login credentials are in valid";
            }
            String token = UUID.randomUUID().toString();
            ChangeEmail changeEmail = new ChangeEmail(changeEmailModel.getNewEmail(),
                    user.getUserId(), token);
            changeEmailRepository.save(changeEmail);

            String url = applicationUrl + "/changeEmail?token=" + token;

            log.info("Click the link for verification {}", url);

            return "You have been sent an email";
        }
        return "Bad Request";
    }

    @Override
    public String EmailChangeValidation(String token, String applicationUrl) {
        Optional<ChangeEmail> changeEmail = changeEmailRepository.FindByOldEmailToken(token);
        if (changeEmail.isPresent()) {
            Calendar calendar = Calendar.getInstance();
            if (changeEmail.get().getExpirationTime().getTime() -
                    calendar.getTime().getTime() <= 0) {
                return "Expired";
            }
            String tkn = UUID.randomUUID().toString();
            ChangeEmail newEmailToken = new ChangeEmail(tkn);
            changeEmail.get().setNewEmailtoken(newEmailToken.getNewEmailtoken());
            changeEmail.get().setExpirationTime(newEmailToken.getExpirationTime());
            changeEmailRepository.save(changeEmail.get());
            String url = applicationUrl + "/saveNewEmail?token=" + tkn;

            log.info("Click the link for verification {}", url);

            return "You have been sent an email to your new email";

        }

        return "Bad Requst";

    }

    @Override
    public String saveNewEmail(String token) {

        Optional<ChangeEmail> changeEmail = changeEmailRepository.FindByNewEmailToken(token);

        if (changeEmail.isPresent()) {

            Calendar calendar = Calendar.getInstance();
            if (changeEmail.get().getExpirationTime().getTime() -
                    calendar.getTime().getTime() <= 0) {
                return "Expired";
            }

            Optional<User> user = userRepository.findById(changeEmail.get().getTargetId());

            if (user.isPresent()) {
                User obj = user.get();
                obj.setEmail(changeEmail.get().getNewEmail());
                changeEmailRepository.delete(changeEmail.get());
                return "Your email has been changed sucessfully";
            } else {
                return "We encountered error unable to fulfill your request";
            }

        }
        return "Bad Requst";
    }

    private ResetToken processToken(ResetToken oldToken) {
        ResetToken newToken = new ResetToken(UUID.randomUUID().toString());
        oldToken.setToken(newToken.getToken());
        oldToken.setExpirationTime(newToken.getExpirationTime());
        resetTokenRepository.save(oldToken);
        return oldToken;
    }

    private boolean validPassword(UserResetModel userResetModel, User user) {

        return passwordEncoder.matches(userResetModel.getPassword(), user.getPassword());
    }

}
