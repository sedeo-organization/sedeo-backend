package com.sedeo.settlement.repository.db.configuration;

import com.sedeo.domain.settlement.db.ExchangeRepository;
import com.sedeo.domain.settlement.db.ParticipantRepository;
import com.sedeo.domain.settlement.db.SettlementGroupRepository;
import com.sedeo.domain.settlement.db.SettlementRepository;
import com.sedeo.settlement.repository.db.ExchangeJdbcRepository;
import com.sedeo.settlement.repository.db.ParticipantJdbcRepository;
import com.sedeo.settlement.repository.db.SettlementGroupJdbcRepository;
import com.sedeo.settlement.repository.db.SettlementJdbcRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

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
    ExchangeRepository exchangeRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new ExchangeJdbcRepository(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Bean
    SettlementRepository settlementRepository(JdbcTemplate jdbcTemplate, ExchangeRepository exchangeRepository) {
        return new SettlementJdbcRepository(jdbcTemplate, exchangeRepository);
    }
}
