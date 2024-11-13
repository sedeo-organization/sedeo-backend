package com.sedeo.settlement.db;

import com.sedeo.common.error.DatabaseError.DatabaseWriteUnsuccessfulError;
import com.sedeo.common.error.DatabaseError.DatabaseReadUnsuccessfulError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.db.modelmapper.SettlementGroupMapper;
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
import static java.lang.Boolean.TRUE;

public class SettlementGroupJdbcRepository implements SettlementGroupRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementGroupJdbcRepository.class);
    private static final com.sedeo.settlement.db.mapper.SettlementGroupMapper SETTLEMENT_GROUP_MAPPER = new com.sedeo.settlement.db.mapper.SettlementGroupMapper();
    private static final SettlementGroupMapper SETTLEMENT_MAPPER = SettlementGroupMapper.INSTANCE;

    public SettlementGroupJdbcRepository(NamedParameterJdbcOperations namedParameterJdbcOperations, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Either<GeneralError, List<SettlementGroup>> findSettlementGroups(List<UUID> groupIds) {
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

    @Override
    public Boolean exists(UUID groupId) {
        return TRUE.equals(jdbcTemplate.queryForObject(SETTLEMENT_GROUP_EXISTS_BY_GROUP_ID, Boolean.class, groupId));
    }
}
