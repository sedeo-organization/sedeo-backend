package com.sedeo.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.Participant;
import com.sedeo.settlement.model.Settlement;
import com.sedeo.settlement.model.view.DetailedSettlement;
import com.sedeo.settlement.model.view.SimpleSettlement;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface Settlements {

    Either<GeneralError, Void> createSettlement(Settlement settlement, UUID userId, UUID groupId);

    Either<GeneralError, List<SimpleSettlement>> fetchSettlements(UUID userId, UUID groupId);

    Either<GeneralError, DetailedSettlement> fetchSettlementDetails(UUID userId, UUID groupId, UUID settlementId);

    Either<GeneralError, Void> settleExchange(UUID userId, UUID groupId, UUID settlementId, UUID exchangeId);

    Either<GeneralError, List<Participant>> fetchParticipants(UUID userId, UUID groupId);
}
