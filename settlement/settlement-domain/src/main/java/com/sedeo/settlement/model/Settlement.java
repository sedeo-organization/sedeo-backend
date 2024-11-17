package com.sedeo.settlement.model;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.error.SettlementGroupError;
import io.vavr.control.Either;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Settlement(UUID settlementId, String title, BigDecimal totalValue, List<Exchange> exchanges) {

    public Either<GeneralError, Settlement> validateExchangeDirections() {
        Optional<Exchange> sameDirectionExchangeExists = this.exchanges()
                .stream()
                .filter(exchange -> exchange.debtorUserId().equals(exchange.creditorUserId()))
                .findAny();

        if (sameDirectionExchangeExists.isPresent()) {
            return Either.left(new SettlementGroupError.SettlementExchangeDirectionIncorrect());
        }
        return Either.right(this);
    }

    public Either<GeneralError, Settlement> validateTotalValue() {
        boolean exchangeValuesDoNotMatchTotalValue = !this.exchanges()
                .stream()
                .map(Exchange::exchangeValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .equals(this.totalValue());

        if (exchangeValuesDoNotMatchTotalValue) {
            return Either.left(new SettlementGroupError.SettlementValuesIncorrect());
        }
        return Either.right(this);
    }

    public Either<GeneralError, Settlement> validateThatParticipantsBelongToGroup(Boolean participantsBelongToGroup) {
        boolean notAllParticipantsBelongToGroup = !participantsBelongToGroup;
        if (notAllParticipantsBelongToGroup) {
            return Either.left(new SettlementGroupError.UserNotAuthorized());
        }
        return Either.right(this);
    }

    public Set<UUID> aggregateSettlementParticipants(UUID creatorId) {
        return this.exchanges()
                .stream()
                .flatMap(exchange -> Stream.of(exchange.creditorUserId(), exchange.debtorUserId(), creatorId))
                .collect(Collectors.toSet());
    }

    public Either<GeneralError, Settlement> settleExchange(UUID userId, UUID exchangeId) {
        Optional<Exchange> lookedForExchange = exchanges.stream()
                .filter(exchange -> exchange.exchangeId().equals(exchangeId))
                .findAny();
        if (lookedForExchange.isEmpty()) {
            return Either.left(new SettlementGroupError.ExchangeDoesNotExist());
        }
        Exchange foundExchange = lookedForExchange.get();

        boolean userIsNotCreditor = !foundExchange.creditorUserId().equals(userId);
        if (userIsNotCreditor) {
            return Either.left(new SettlementGroupError.UserNotAuthorized());
        }

        boolean statusChangeIsPossible = ExchangeStatus.SETTLED.isStatusChangePossible(foundExchange.status());
        if (statusChangeIsPossible) {
            foundExchange = foundExchange.withSettledStatus();
        } else {
            return Either.left(new SettlementGroupError.ExchangeStatusChangeNotAllowed());
        }

        exchanges.remove(foundExchange);
        exchanges.add(foundExchange);
        return Either.right(new Settlement(this.settlementId, this.title, this.totalValue, this.exchanges));
    }

    public Either<GeneralError, Exchange> singleExchange(UUID exchangeId) {
        Optional<Exchange> lookedForExchange = exchanges.stream()
                .filter(exchange -> exchange.exchangeId().equals(exchangeId))
                .findAny();
        if (lookedForExchange.isEmpty()) {
            return Either.left(new SettlementGroupError.ExchangeDoesNotExist());
        }
        return Either.right(lookedForExchange.get());
    }

    public Settlement withExchanges(List<Exchange> exchanges) {
        return new Settlement(this.settlementId, this.title, this.totalValue, exchanges);
    }
}
