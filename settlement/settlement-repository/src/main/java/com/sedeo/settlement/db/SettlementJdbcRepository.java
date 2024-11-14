package com.sedeo.settlement.db;

import com.sedeo.common.error.DatabaseError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.db.mapper.SettlementEntityMapper;
import com.sedeo.settlement.db.modelmapper.SettlementMapper;
import com.sedeo.settlement.model.Settlement;
import com.sedeo.settlement.model.view.SimpleSettlement;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

import static com.sedeo.settlement.db.queries.SettlementGroupQuery.SETTLEMENT_GROUP_EXISTS_BY_GROUP_ID;
import static com.sedeo.settlement.db.queries.SettlementQuery.*;
import static java.lang.Boolean.TRUE;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

public class SettlementJdbcRepository implements SettlementRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementJdbcRepository.class);
    private static final SettlementEntityMapper SETTLEMENT_ENTITY_MAPPER = new SettlementEntityMapper();
    private static final SettlementMapper SETTLEMENT_MAPPER = SettlementMapper.INSTANCE;

    private final JdbcTemplate jdbcTemplate;
    private final ExchangeRepository exchangeRepository;

    public SettlementJdbcRepository(JdbcTemplate jdbcTemplate, ExchangeRepository exchangeRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    public Either<GeneralError, Settlement> save(Settlement settlement, UUID groupId) {
        return Try.of(() -> jdbcTemplate.update(SAVE_SETTLEMENT, settlement.settlementId(), groupId, settlement.title(), settlement.totalValue()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> exchangeRepository.save(settlement.exchanges(), settlement.settlementId(), groupId))
                .map(result -> settlement)
                .mapLeft(DatabaseError.DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<SimpleSettlement>> find(UUID groupId) {
        return Try.of(() -> jdbcTemplate.query(SETTLEMENTS_BY_GROUP_ID, SETTLEMENT_ENTITY_MAPPER, groupId))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .map(SETTLEMENT_MAPPER::settlementEntityListToSimpleSettlementList)
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, Settlement> findSettlement(UUID settlementId) {
        return Try.of(() -> singleResult(jdbcTemplate.query(SETTLEMENT_BY_SETTLEMENT_ID, SETTLEMENT_ENTITY_MAPPER, settlementId)))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(throwable -> (GeneralError) new DatabaseError.DatabaseReadUnsuccessfulError(throwable))
                .flatMap(settlementEntity -> exchangeRepository.find(settlementId)
                        .map(exchanges -> SETTLEMENT_MAPPER.settlementEntityToSettlement(settlementEntity, exchanges)));
    }

    @Override
    public Boolean exists(UUID settlementId) {
        return TRUE.equals(jdbcTemplate.queryForObject(SETTLEMENT_EXISTS_BY_SETTLEMENT_ID, Boolean.class, settlementId));
    }

    @Override
    public Either<GeneralError, Settlement> update(Settlement settlement, UUID groupId) {
        return Try.of(() -> jdbcTemplate.update(UPDATE_SETTLEMENT, groupId, settlement.title(), settlement.totalValue(), settlement.settlementId()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .mapLeft(exception -> (GeneralError) new DatabaseError.DatabaseWriteUnsuccessfulError(exception))
                .flatMap(result -> exchangeRepository.update(settlement.exchanges(), settlement.settlementId(), groupId))
                .map(result -> settlement);
    }
}
