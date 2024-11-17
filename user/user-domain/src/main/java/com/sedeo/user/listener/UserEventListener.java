package com.sedeo.user.listener;

import com.sedeo.event.ExchangeCreatedEvent;
import com.sedeo.event.ExchangeSettledEvent;
import com.sedeo.user.facade.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

public class UserEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventListener.class);

    private final Users users;

    public UserEventListener(Users users) {
        this.users = users;
    }

    @EventListener
    @Transactional
    public void handleExchangeCreatedEvent(ExchangeCreatedEvent exchangeCreatedEvent) {
        ExchangeCreatedEvent.ExchangeCreatedModel exchangeCreatedModel = exchangeCreatedEvent.getExchangeCreatedModel();
        users.addToUsersAccountBalance(exchangeCreatedModel.debtorUserId(), exchangeCreatedModel.exchangeValue())
                .peekLeft(error -> LOGGER.warn("Users account balance update failed"))
                .peek(user -> LOGGER.info("Users account balance update successful successfully"));
        users.subtractFromUsersAccountBalance(exchangeCreatedModel.creditorUserId(), exchangeCreatedModel.exchangeValue())
                .peekLeft(error -> LOGGER.warn("Users account balance update failed"))
                .peek(user -> LOGGER.info("Users account balance update successful successfully"));
    }

    @EventListener
    public void handleExchangeSettledEvent(ExchangeSettledEvent exchangeSettledEvent) {
        ExchangeSettledEvent.ExchangeSettledModel exchangeSettledModel = exchangeSettledEvent.getExchangeSettledModel();
        users.addToUsersAccountBalance(exchangeSettledModel.creditorUserId(), exchangeSettledModel.exchangeValue())
                .peekLeft(error -> LOGGER.warn("Users account balance update failed"))
                .peek(user -> LOGGER.info("Users account balance update successful successfully"));
        users.subtractFromUsersAccountBalance(exchangeSettledModel.debtorUserId(), exchangeSettledModel.exchangeValue())
                .peekLeft(error -> LOGGER.warn("Users account balance update failed"))
                .peek(user -> LOGGER.info("Users account balance update successful successfully"));
    }
}
