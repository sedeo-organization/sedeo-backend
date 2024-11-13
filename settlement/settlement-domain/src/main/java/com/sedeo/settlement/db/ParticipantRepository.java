package com.sedeo.settlement.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.Participant;
import com.sedeo.settlement.model.SettlementStatus;
import io.vavr.control.Either;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ParticipantRepository {

    Either<GeneralError, List<UUID>> findGroupIdsThatParticipantBelongsTo(UUID userId, List<SettlementStatus> statuses);

    Either<GeneralError, Participant> save(Participant participant);

    Either<GeneralError, List<Participant>> save(List<Participant> participants);

    Either<GeneralError, List<Participant>> findParticipantsForGroup(UUID groupId);

    Boolean exists(UUID groupId, UUID userId);

    Boolean exist(UUID groupId, Set<UUID> userIds);
}
