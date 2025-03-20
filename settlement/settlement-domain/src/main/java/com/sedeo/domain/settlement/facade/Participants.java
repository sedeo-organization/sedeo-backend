package com.sedeo.domain.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.settlement.model.SettlementStatus;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface Participants {

    Either<GeneralError, Void> changeParticipantsStatus(List<UUID> participantIds, UUID groupId, SettlementStatus status);

    Either<GeneralError, Void> verifyParticipantForStatusChange(UUID participantId, UUID groupId);
}
