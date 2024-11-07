package com.sedeo.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.db.SettlementGroupRepository;
import com.sedeo.settlement.db.ParticipantRepository;
import com.sedeo.settlement.model.SettlementGroup;
import com.sedeo.settlement.model.SettlementStatus;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public class SettlementGroupsFacade implements SettlementGroups {

    private final SettlementGroupRepository settlementGroupRepository;
    private final ParticipantRepository participantRepository;

    public SettlementGroupsFacade(SettlementGroupRepository settlementGroupRepository, ParticipantRepository participantRepository) {
        this.settlementGroupRepository = settlementGroupRepository;
        this.participantRepository = participantRepository;
    }

    @Override
    public Either<GeneralError, List<SettlementGroup>> fetchSettlementGroups(UUID userId, List<SettlementStatus> statuses) {
        return participantRepository.findGroupIdsThatParticipantBelongsTo(userId, statuses)
                .flatMap(settlementGroupRepository::findUsersSettlementGroups);
    }
}
