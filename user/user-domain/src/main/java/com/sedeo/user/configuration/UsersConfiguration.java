package com.sedeo.user.configuration;

import com.sedeo.user.db.PasswordResetTokenRepository;
import com.sedeo.user.db.UserRepository;
import com.sedeo.user.facade.Users;
import com.sedeo.user.facade.UsersFacade;
import com.sedeo.user.listener.UserEventListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsersConfiguration {

    @Bean
    Users users(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository,
                ApplicationEventPublisher applicationEventPublisher) {
        return new UsersFacade(userRepository, passwordResetTokenRepository, applicationEventPublisher);
    }

    @Bean
    UserEventListener userEventListener(Users users) {
        return new UserEventListener(users);
    }
}
