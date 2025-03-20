package com.sedeo.domain.settlement.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.settlement.model.Exchange;
import com.sedeo.domain.settlement.model.view.SummaryExchange;
import com.sedeo.domain.settlement.model.ExchangeStatus;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface ExchangeRepository {

    Either<GeneralError, List<Exchange>> save(List<Exchange> exchanges, UUID groupId, UUID settlementId);

    Either<GeneralError, List<Exchange>> find(UUID settlementId);

    Either<GeneralError, List<Exchange>> update(List<Exchange> exchanges, UUID groupId, UUID settlementId);

    Either<GeneralError, List<Exchange>> updateExchangesStatus(List<Exchange> exchanges);

    Either<GeneralError, List<Exchange>> findExchangesInvolvingParticipant(UUID groupId, UUID participantId);

    Either<GeneralError, List<Exchange>> findExchangesInvolvingParticipantsWithinGroupByStatuses(UUID groupId, UUID firstParticipantId,
                                                                                                 UUID secondParticipantId, List<ExchangeStatus> statuses);

    Either<GeneralError, List<SummaryExchange>> aggregateExchangesGroupSummary(UUID groupId, List<ExchangeStatus> statuses);
}
