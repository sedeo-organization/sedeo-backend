package com.sedeo.settlement.configuration;

import com.sedeo.settlement.db.ExchangeRepository;
import com.sedeo.settlement.db.SettlementGroupRepository;
import com.sedeo.settlement.db.ParticipantRepository;
import com.sedeo.settlement.db.SettlementRepository;
import com.sedeo.settlement.facade.*;
import com.sedeo.settlement.listener.SettlementEventListener;
import com.sedeo.user.facade.Users;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
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
    Settlements settlements(SettlementRepository settlementRepository, ParticipantRepository participantRepository,
                            ApplicationEventPublisher applicationEventPublisher) {
        return new SettlementsFacade(settlementRepository, participantRepository, applicationEventPublisher);
    }

    @Bean
    Participants participants(ParticipantRepository participantRepository, ExchangeRepository exchangeRepository) {
        return new ParticipantsFacade(participantRepository, exchangeRepository);
    }

    @Bean
    SettlementEventListener settlementEventListener(Participants participants) {
        return new SettlementEventListener(participants);
    }
}
