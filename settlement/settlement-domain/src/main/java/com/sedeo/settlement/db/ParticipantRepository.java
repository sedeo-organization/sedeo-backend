package com.sedeo.settlement.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.Participant;
import com.sedeo.settlement.model.SettlementStatus;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface ParticipantRepository {

    Either<GeneralError, List<UUID>> findGroupIdsThatParticipantBelongsTo(UUID userId, List<SettlementStatus> statuses);
}
