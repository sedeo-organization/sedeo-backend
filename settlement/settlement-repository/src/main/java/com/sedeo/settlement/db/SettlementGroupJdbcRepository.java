package com.sedeo.settlement.db;

import com.sedeo.common.error.DatabaseError.DatabaseWriteUnsuccessfulError;
import com.sedeo.common.error.DatabaseError.DatabaseReadUnsuccessfulError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.db.mapper.SettlementGroupMapper;
import com.sedeo.settlement.model.SettlementGroup;
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
import java.util.UUID;

import static com.sedeo.settlement.db.queries.SettlementGroupQuery.*;

public class SettlementGroupJdbcRepository implements SettlementGroupRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementGroupJdbcRepository.class);
    private static final SettlementGroupMapper SETTLEMENT_GROUP_MAPPER = new SettlementGroupMapper();
    private static final SettlementMapper SETTLEMENT_MAPPER = SettlementMapper.INSTANCE;

    public SettlementGroupJdbcRepository(NamedParameterJdbcOperations namedParameterJdbcOperations, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Either<GeneralError, List<SettlementGroup>> findUsersSettlementGroups(List<UUID> groupIds) {
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(GROUP_IDS_PARAMETER, groupIds));

        return Try.of(() -> namedParameterJdbcOperations.query(FIND_GROUPS_BY_GROUP_IDS, parameters, SETTLEMENT_GROUP_MAPPER))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .map(SETTLEMENT_MAPPER::settlementGroupEntityListToSettlementGroup)
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, SettlementGroup> save(SettlementGroup settlementGroup) {
        return Try.of(() -> jdbcTemplate.update(SAVE_SETTLEMENT_GROUP,
                        settlementGroup.groupId(),
                        settlementGroup.title()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> settlementGroup)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }
}
