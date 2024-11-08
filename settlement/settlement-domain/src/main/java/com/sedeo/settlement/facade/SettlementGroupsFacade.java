package com.sedeo.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.db.SettlementGroupRepository;
import com.sedeo.settlement.db.ParticipantRepository;
import com.sedeo.settlement.model.Participant;
import com.sedeo.settlement.model.SettlementGroup;
import com.sedeo.settlement.model.SettlementStatus;
import io.vavr.control.Either;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.sedeo.settlement.model.SettlementStatus.PENDING;

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

    @Override
    @Transactional
    public Either<GeneralError, Void> createSettlementGroup(UUID groupId, UUID userId, String title) {
        SettlementGroup settlementGroup = new SettlementGroup(groupId, title);
        Participant groupParticipant = new Participant(groupId, userId, PENDING);

        return settlementGroupRepository.save(settlementGroup)
                .flatMap(group -> participantRepository.save(groupParticipant))
                .flatMap(participant -> Either.right(null));
    }
}
