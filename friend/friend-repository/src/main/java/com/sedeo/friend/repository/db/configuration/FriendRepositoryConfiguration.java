package com.sedeo.friend.repository.db.configuration;

import com.sedeo.domain.friend.db.DetailedFriendshipInvitationRepository;
import com.sedeo.domain.friend.db.FriendRepository;
import com.sedeo.domain.friend.db.FriendshipInvitationRepository;
import com.sedeo.domain.friend.db.FriendshipRepository;
import com.sedeo.friend.repository.db.DetailedFriendshipInvitationJdbcRepository;
import com.sedeo.friend.repository.db.FriendJdbcRepository;
import com.sedeo.friend.repository.db.FriendshipInvitationJdbcRepository;
import com.sedeo.friend.repository.db.FriendshipJdbcRepository;
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
