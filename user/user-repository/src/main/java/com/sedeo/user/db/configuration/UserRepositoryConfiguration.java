package com.sedeo.user.db.configuration;

import com.sedeo.user.db.UserJdbcRepository;
import com.sedeo.user.db.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class UserRepositoryConfiguration {

    @Bean
    UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new UserJdbcRepository(jdbcTemplate);
    }
}
