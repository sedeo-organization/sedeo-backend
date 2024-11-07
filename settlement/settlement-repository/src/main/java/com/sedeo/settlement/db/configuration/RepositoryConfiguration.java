package com.sedeo.settlement.db.configuration;

import com.sedeo.settlement.db.ParticipantJdbcRepository;
import com.sedeo.settlement.db.ParticipantRepository;
import com.sedeo.settlement.db.SettlementGroupJdbcRepository;
import com.sedeo.settlement.db.SettlementGroupRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@Configuration
public class RepositoryConfiguration {

    @Bean
    SettlementGroupRepository settlementGroupRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        return new SettlementGroupJdbcRepository(namedParameterJdbcOperations);
    }

    @Bean
    ParticipantRepository settlementParticipantRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        return new ParticipantJdbcRepository(namedParameterJdbcOperations);
    }
}
