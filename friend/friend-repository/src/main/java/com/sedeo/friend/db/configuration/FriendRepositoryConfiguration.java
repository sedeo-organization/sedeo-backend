package com.sedeo.friend.db.configuration;

import com.sedeo.friend.db.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@Configuration
public class FriendRepositoryConfiguration {

    @Bean
    FriendRepository friendRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcOperations namedParameterJdbcOperations) {
        return new FriendJdbcRepository(jdbcTemplate, namedParameterJdbcOperations);
    }

    @Bean
    FriendshipRepository friendshipRepository(JdbcTemplate jdbcTemplate) {
        return new FriendshipJdbcRepository(jdbcTemplate);
    }

    @Bean
    FriendshipInvitationRepository friendshipInvitationRepository(JdbcTemplate jdbcTemplate) {
        return new FriendshipInvitationJdbcRepository(jdbcTemplate);
    }

    @Bean
    DetailedFriendshipInvitationRepository detailedFriendshipInvitationRepository(JdbcTemplate jdbcTemplate) {
        return new DetailedFriendshipInvitationJdbcRepository(jdbcTemplate);
    }
}
