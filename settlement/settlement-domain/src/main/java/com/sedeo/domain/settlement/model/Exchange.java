package com.sedeo.domain.settlement.model;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.settlement.model.error.SettlementGroupError;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.math.BigDecimal;
import java.util.UUID;

public record Exchange(UUID exchangeId, UUID debtorUserId, UUID creditorUserId, BigDecimal exchangeValue, ExchangeStatus status) {

    public Either<GeneralError, Exchange> withChangedStatus(ExchangeStatus newStatus) {
        return Option.of(this.status)
                .filter(newStatus::isStatusChangePossible)
                .map(currentStatus -> new Exchange(this.exchangeId, this.debtorUserId, this.creditorUserId, this.exchangeValue, newStatus))
                .toEither(new SettlementGroupError.ExchangeStatusChangeNotAllowed());
    }

    public Exchange withPendingStatus() {
        return new Exchange(this.exchangeId, this.debtorUserId, this.creditorUserId, this.exchangeValue, ExchangeStatus.PENDING);
    }

    public Exchange withSettledStatus() {
        return new Exchange(this.exchangeId, this.debtorUserId, this.creditorUserId, this.exchangeValue, ExchangeStatus.SETTLED);
    }
}
