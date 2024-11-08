package com.sedeo.settlement.db.configuration;

import com.sedeo.settlement.db.ParticipantJdbcRepository;
import com.sedeo.settlement.db.ParticipantRepository;
import com.sedeo.settlement.db.SettlementGroupJdbcRepository;
import com.sedeo.settlement.db.SettlementGroupRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@Configuration
public class RepositoryConfiguration {

    @Bean
    SettlementGroupRepository settlementGroupRepository(NamedParameterJdbcOperations namedParameterJdbcOperations, JdbcTemplate jdbcTemplate) {
        return new SettlementGroupJdbcRepository(namedParameterJdbcOperations, jdbcTemplate);
    }

    @Bean
    ParticipantRepository settlementParticipantRepository(NamedParameterJdbcOperations namedParameterJdbcOperations, JdbcTemplate jdbcTemplate) {
        return new ParticipantJdbcRepository(namedParameterJdbcOperations, jdbcTemplate);
    }
}
