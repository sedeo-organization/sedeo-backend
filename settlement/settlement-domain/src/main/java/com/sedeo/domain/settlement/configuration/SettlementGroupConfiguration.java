package com.sedeo.domain.settlement.configuration;

import com.sedeo.domain.settlement.db.ExchangeRepository;
import com.sedeo.domain.settlement.db.ParticipantRepository;
import com.sedeo.domain.settlement.db.SettlementGroupRepository;
import com.sedeo.domain.settlement.db.SettlementRepository;
import com.sedeo.domain.settlement.facade.*;
import com.sedeo.domain.settlement.listener.SettlementEventListener;
import com.sedeo.domain.user.facade.Users;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SettlementGroupConfiguration {

    @Bean
    SettlementGroups settlementGroups(SettlementGroupRepository settlementGroupRepository, ParticipantRepository participantRepository,
                                      Users users, ExchangeRepository exchangeRepository) {
        return new SettlementGroupsFacade(settlementGroupRepository, participantRepository, users, exchangeRepository);
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

    @Bean
    Exchanges exchanges(ExchangeRepository exchangeRepository, ParticipantRepository participantRepository, ApplicationEventPublisher applicationEventPublisher) {
        return new ExchangesFacade(exchangeRepository, participantRepository, applicationEventPublisher);
    }
}
