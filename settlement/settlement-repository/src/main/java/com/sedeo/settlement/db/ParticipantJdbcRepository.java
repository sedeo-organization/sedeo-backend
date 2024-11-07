package com.sedeo.settlement.db;

import com.sedeo.common.error.DatabaseError.DatabaseReadUnsuccessfulError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.SettlementStatus;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.sedeo.settlement.db.queries.ParticipantQuery.*;

public class ParticipantJdbcRepository implements ParticipantRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantJdbcRepository.class);

    public ParticipantJdbcRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
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
}
