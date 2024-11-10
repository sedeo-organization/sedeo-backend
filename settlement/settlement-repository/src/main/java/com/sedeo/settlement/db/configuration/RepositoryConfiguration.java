package com.sedeo.settlement.db.configuration;

import com.sedeo.settlement.db.*;
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

    @Bean
    ExchangeRepository exchangeRepository(JdbcTemplate jdbcTemplate) {
        return new ExchangeJdbcRepository(jdbcTemplate);
    }

    @Bean
    SettlementRepository settlementRepository(JdbcTemplate jdbcTemplate, ExchangeRepository exchangeRepository) {
        return new SettlementJdbcRepository(jdbcTemplate, exchangeRepository);
    }
}
