package com.sedeo.settlement.db;

import com.sedeo.common.error.DatabaseError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.Settlement;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static com.sedeo.settlement.db.queries.SettlementQuery.SAVE_SETTLEMENT;

public class SettlementJdbcRepository implements SettlementRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementJdbcRepository.class);

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
}
