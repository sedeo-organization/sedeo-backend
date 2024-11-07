package com.sedeo.settlement.configuration;

import com.sedeo.settlement.db.SettlementGroupRepository;
import com.sedeo.settlement.db.ParticipantRepository;
import com.sedeo.settlement.facade.SettlementGroups;
import com.sedeo.settlement.facade.SettlementGroupsFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SettlementGroupConfiguration {

    @Bean
    SettlementGroups settlementGroups(SettlementGroupRepository settlementGroupRepository, ParticipantRepository participantRepository) {
        return new SettlementGroupsFacade(settlementGroupRepository, participantRepository);
    }
}
