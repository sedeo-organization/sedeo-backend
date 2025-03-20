package com.sedeo.user.repository.db.configuration;

import com.sedeo.user.repository.db.PasswordResetTokenJdbcRepository;
import com.sedeo.domain.user.db.PasswordResetTokenRepository;
import com.sedeo.user.repository.db.UserJdbcRepository;
import com.sedeo.domain.user.db.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@Configuration
public class UserRepositoryConfiguration {

    @Bean
    UserRepository userRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcOperations namedParameterJdbcOperations) {
        return new UserJdbcRepository(jdbcTemplate, namedParameterJdbcOperations);
    }

    @Bean
    PasswordResetTokenRepository passwordResetTokenRepository(JdbcTemplate jdbcTemplate) {
        return new PasswordResetTokenJdbcRepository(jdbcTemplate);
    }
}
