package com.sedeo.authentication.configuration;

import com.sedeo.authentication.services.*;
import com.sedeo.user.facade.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@Configuration
public class AuthenticationServiceConfiguration {

    @Bean
    JwtConfiguration jwtConfiguration(@Value("${jwt.duration.time.in.seconds}") Long jwtDurationTimeInSeconds) {
        return new JwtConfiguration(jwtDurationTimeInSeconds);
    }

    @Bean
    JwtService jwtService(JwtEncoder jwtEncoder, JwtConfiguration jwtConfiguration) {
        return new JwtBaseService(jwtEncoder, jwtConfiguration);
    }

    @Bean
    UserAuthenticationService userAuthenticationService(Users users, PasswordEncoder passwordEncoder, JwtService jwtService) {
        return new UserAuthenticationBaseService(users, passwordEncoder, jwtService);
    }
}
