package com.sedeo.settlement.facade;

import com.sedeo.common.error.GeneralError;
import io.vavr.control.Either;

import java.util.UUID;

public interface Exchanges {

    Either<GeneralError, Void> bulkSettleExchangesForParticipantsWithinGroup(UUID groupId, UUID debtorUserId, UUID creditorUserId, UUID userId);
}
