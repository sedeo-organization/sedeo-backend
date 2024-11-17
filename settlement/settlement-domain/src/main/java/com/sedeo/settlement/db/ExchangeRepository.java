package com.sedeo.settlement.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.Exchange;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface ExchangeRepository {

    Either<GeneralError, List<Exchange>> save(List<Exchange> exchanges, UUID groupId, UUID settlementId);

    Either<GeneralError, List<Exchange>> find(UUID settlementId);

    Either<GeneralError, List<Exchange>> update(List<Exchange> exchanges, UUID groupId, UUID settlementId);

    Either<GeneralError, List<Exchange>> findExchangesInvolvingParticipant(UUID groupId, UUID participantId);
}
