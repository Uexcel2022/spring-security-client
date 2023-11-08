package com.uexcel.spring.security.client.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    final String[] WHITE_LIST_URLS = { "/hello", "/register", "/verifyRegistration" };

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    // @Bean
    // WebSecurityCustomizer webSecurityCustomizer() {

    // return (web) -> web.ignoring().requestMatchers(WHITE_LIST_URLS);

    // }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cs -> cs.disable())
                .csrf(f -> f.disable());

        return http.build();

    }
}
