package com.sedeo.settlement.repository.db;

import com.sedeo.common.error.DatabaseError.DatabaseReadUnsuccessfulError;
import com.sedeo.common.error.DatabaseError.DatabaseWriteUnsuccessfulError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.settlement.db.ParticipantRepository;
import com.sedeo.settlement.repository.db.mapper.ParticipantEntityMapper;
import com.sedeo.settlement.repository.db.modelmapper.ParticipantMapper;
import com.sedeo.domain.settlement.model.Participant;
import com.sedeo.domain.settlement.model.SettlementStatus;
import io.vavr.collection.Traversable;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.sedeo.settlement.repository.db.queries.ParticipantQuery.*;
import static java.lang.Boolean.TRUE;

public class ParticipantJdbcRepository implements ParticipantRepository {

    private static final ParticipantEntityMapper PARTICIPANT_ENTITY_MAPPER = new ParticipantEntityMapper();
    private static final ParticipantMapper PARTICIPANT_MAPPER = ParticipantMapper.INSTANCE;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantJdbcRepository.class);

    public ParticipantJdbcRepository(NamedParameterJdbcOperations namedParameterJdbcOperations, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Either<GeneralError, List<UUID>> findGroupIdsThatParticipantBelongsTo(UUID userId, List<SettlementStatus> statuses) {
        List<String> settlementStatuses = statuses.stream().map(Enum::toString).toList();
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
                STATUSES_PARAMETER, settlementStatuses,
                USER_ID_PARAMETER, userId));

        return Try.of(() -> namedParameterJdbcOperations.queryForList(FIND_GROUP_IDS_FOR_PARTICIPANT_USER_ID_AND_SETTLEMENT_STATUS, parameters, UUID.class))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, Participant> save(Participant participant) {
        return Try.of(() -> jdbcTemplate.update(SAVE_PARTICIPANT, participant.groupId(), participant.userId(), participant.firstName(),
                        participant.lastName(), participant.status().toString()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> participant)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<Participant>> save(List<Participant> participants) {
        return Either.sequence(participants.stream().map(this::save).toList())
                .map(savedParticipants -> participants)
                .mapLeft(Traversable::head);
    }

    @Override
    public Either<GeneralError, Participant> update(Participant participant) {
        return Try.of(() -> jdbcTemplate.update(UPDATE_PARTICIPANT, participant.firstName(), participant.lastName(),
                        participant.status().name(), participant.groupId(), participant.userId()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> participant)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<Participant>> findParticipantsForGroup(UUID groupId) {
        return Try.of(() -> jdbcTemplate.query(FIND_PARTICIPANTS_BY_GROUP_ID, PARTICIPANT_ENTITY_MAPPER, groupId))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .map(PARTICIPANT_MAPPER::participantEntitiesToParticipants)
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<Participant>> findParticipantsForGroup(List<UUID> participantIds, UUID groupId) {
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
                GROUP_ID_PARAMETER, groupId,
                USER_IDS_PARAMETER, participantIds));

        return Try.of(() -> namedParameterJdbcOperations.query(FIND_PARTICIPANTS_BY_GROUP_ID_AND_USER_IDS, parameters, PARTICIPANT_ENTITY_MAPPER))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .map(PARTICIPANT_MAPPER::participantEntitiesToParticipants)
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Boolean exists(UUID groupId, UUID userId) {
        return TRUE.equals(jdbcTemplate.queryForObject(PARTICIPANT_EXISTS_BY_GROUP_ID_AND_USER_ID, Boolean.class, groupId, userId));
    }

    @Override
    public Boolean exist(UUID groupId, Set<UUID> userIds) {
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
                USER_IDS_SIZE_PARAMETER, userIds.size(),
                GROUP_ID_PARAMETER, groupId,
                USER_IDS_PARAMETER, userIds));

        return TRUE.equals(namedParameterJdbcOperations.queryForObject(PARTICIPANTS_EXIST_BY_GROUP_ID_AND_USER_IDS, parameters, Boolean.class));
    }
}
