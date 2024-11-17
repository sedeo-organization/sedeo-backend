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

    Either<GeneralError, List<Participant>> findParticipantsForGroup(UUID groupId);

    Either<GeneralError, List<Participant>> findParticipantsForGroup(List<UUID> participantIds, UUID groupId);

    Either<GeneralError, Participant> save(Participant participant);

    Either<GeneralError, List<Participant>> save(List<Participant> participants);

    Either<GeneralError, Participant> update(Participant participant);

    Boolean exists(UUID groupId, UUID userId);

    Boolean exist(UUID groupId, Set<UUID> userIds);
}
