package com.uexcel.spring.security.client.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    final String[] WHITE_LIST_URLS = { "/hello", "/register", "/verifyRegistration", "/    resendVerificationToken",
            "/resetPassword", "/resendResetPasswordToken", "/changePassword",
            "/changeName", "/changeEmail", "/saveNewEmail" };

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cs -> cs.disable())
                .csrf(rf -> {
                    try {
                        rf.disable()
                                // .exceptionHandling(exception ->
                                // exception.authenticationEntryPoint(authEntryPoint))
                                .sessionManagement(
                                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                        .requestMatchers(WHITE_LIST_URLS)
                                        .permitAll()
                                        .anyRequest().authenticated());
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                });

        return http.build();

    }
}