package com.sedeo.domain.user.configuration;

import com.sedeo.domain.user.db.UserRepository;
import com.sedeo.domain.user.facade.Users;
import com.sedeo.domain.user.facade.UsersFacade;
import com.sedeo.domain.user.listener.UserEventListener;
import com.sedeo.domain.user.db.PasswordResetTokenRepository;
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
