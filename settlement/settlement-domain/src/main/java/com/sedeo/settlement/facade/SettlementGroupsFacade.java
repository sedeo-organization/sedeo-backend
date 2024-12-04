package com.sedeo.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.db.ExchangeRepository;
import com.sedeo.settlement.db.ParticipantRepository;
import com.sedeo.settlement.db.SettlementGroupRepository;
import com.sedeo.settlement.model.ExchangeStatus;
import com.sedeo.settlement.model.Participant;
import com.sedeo.settlement.model.SettlementGroup;
import com.sedeo.settlement.model.SettlementStatus;
import com.sedeo.settlement.model.error.SettlementGroupError;
import com.sedeo.settlement.model.error.SettlementGroupError.SettlementGroupAlreadyExists;
import com.sedeo.settlement.model.view.SummaryExchange;
import com.sedeo.user.facade.Users;
import com.sedeo.user.model.User;
import io.vavr.control.Either;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.sedeo.settlement.model.SettlementStatus.PENDING;

public class SettlementGroupsFacade implements SettlementGroups {

    private final SettlementGroupRepository settlementGroupRepository;
    private final ParticipantRepository participantRepository;
    private final ExchangeRepository exchangeRepository;
    private final Users users;

    public SettlementGroupsFacade(SettlementGroupRepository settlementGroupRepository, ParticipantRepository participantRepository,
                                  Users users, ExchangeRepository exchangeRepository) {
        this.settlementGroupRepository = settlementGroupRepository;
        this.participantRepository = participantRepository;
        this.users = users;
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    public Either<GeneralError, List<SettlementGroup>> fetchSettlementGroups(UUID userId, List<SettlementStatus> statuses) {
        return participantRepository.findGroupIdsThatParticipantBelongsTo(userId, statuses)
                .flatMap(settlementGroupRepository::findSettlementGroups);
    }

    @Override
    @Transactional
    public Either<GeneralError, Void> createSettlementGroup(UUID groupId, String title, Set<UUID> userIds) {
        //TODO: check if creating user is friends with all other users
        if (settlementGroupRepository.exists(groupId)) {
            return Either.left(new SettlementGroupAlreadyExists());
        }

        Either<GeneralError, List<User>> foundUsers = users.fetchUsers(userIds.stream().toList());
        SettlementGroup settlementGroup = new SettlementGroup(groupId, title);

        return foundUsers.map(users -> users.stream()
                .map(user -> new Participant(groupId, user.userId(), user.firstName(), user.lastName(), PENDING)).toList())
                .flatMap(participantRepository::save)
                .flatMap(participants -> settlementGroupRepository.save(settlementGroup))
                .flatMap(groups -> Either.right(null));
    }

    @Override
    public Either<GeneralError, List<SummaryExchange>> fetchSettlementGroupSummary(UUID groupId, UUID userId, List<ExchangeStatus> statuses) {
        if (!participantRepository.exists(groupId, userId)) {
            return Either.left(new SettlementGroupError.UserNotAuthorized());
        }

        return exchangeRepository.aggregateExchangesGroupSummary(groupId, statuses);
    }
}
