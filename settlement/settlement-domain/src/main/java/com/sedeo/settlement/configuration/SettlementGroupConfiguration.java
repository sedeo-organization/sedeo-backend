package com.sedeo.settlement.configuration;

import com.sedeo.settlement.db.SettlementGroupRepository;
import com.sedeo.settlement.db.ParticipantRepository;
import com.sedeo.settlement.db.SettlementRepository;
import com.sedeo.settlement.facade.SettlementGroups;
import com.sedeo.settlement.facade.SettlementGroupsFacade;
import com.sedeo.settlement.facade.Settlements;
import com.sedeo.settlement.facade.SettlementsFacade;
import com.sedeo.user.facade.Users;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SettlementGroupConfiguration {

    @Bean
    SettlementGroups settlementGroups(SettlementGroupRepository settlementGroupRepository, ParticipantRepository participantRepository,
                                      Users users) {
        return new SettlementGroupsFacade(settlementGroupRepository, participantRepository, users);
    }

    @Bean
    Settlements settlements(SettlementRepository settlementRepository, ParticipantRepository participantRepository) {
        return new SettlementsFacade(settlementRepository, participantRepository);
    }
}
