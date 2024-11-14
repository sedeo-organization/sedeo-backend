package com.sedeo.settlement.db;

import com.sedeo.common.error.DatabaseError;
import com.sedeo.common.error.DatabaseError.DatabaseWriteUnsuccessfulError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.db.mapper.ExchangeEntityMapper;
import com.sedeo.settlement.db.modelmapper.ExchangeMapper;
import com.sedeo.settlement.model.Exchange;
import io.vavr.collection.Traversable;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

import static com.sedeo.settlement.db.queries.ExchangeQuery.*;

public class ExchangeJdbcRepository implements ExchangeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeJdbcRepository.class);
    private static final ExchangeEntityMapper EXCHANGE_ENTITY_MAPPER = new ExchangeEntityMapper();
    private static final ExchangeMapper EXCHANGE_MAPPER = ExchangeMapper.INSTANCE;

    private final JdbcTemplate jdbcTemplate;

    public ExchangeJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Either<GeneralError, List<Exchange>> save(List<Exchange> exchanges, UUID groupId, UUID settlementId) {
        return Either.sequence(exchanges.stream().map(exchange -> this.save(exchange, settlementId, groupId)).toList())
                .map(savedExchanges -> exchanges)
                .mapLeft(Traversable::head);
    }

    @Override
    public Either<GeneralError, List<Exchange>> find(UUID settlementId) {
        return Try.of(() -> jdbcTemplate.query(EXCHANGES_BY_SETTLEMENT_ID, EXCHANGE_ENTITY_MAPPER, settlementId))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .map(EXCHANGE_MAPPER::exchangeEntityListToExchangeList)
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<Exchange>> update(List<Exchange> exchanges, UUID groupId, UUID settlementId) {
        return Either.sequence(exchanges.stream().map(exchange -> this.update(exchange, settlementId, groupId)).toList())
                .map(updatedExchanges -> exchanges)
                .mapLeft(Traversable::head);
    }

    private Either<GeneralError, Exchange> update(Exchange exchange, UUID groupId, UUID settlementId) {
        return Try.of(() -> jdbcTemplate.update(UPDATE_EXCHANGE, settlementId, groupId, exchange.debtorUserId(), exchange.creditorUserId(),
                        exchange.exchangeValue(), exchange.status().name(), exchange.exchangeId()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> exchange)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }

    private Either<GeneralError, Exchange> save(Exchange exchange, UUID groupId, UUID settlementId) {
        return Try.of(() -> jdbcTemplate.update(SAVE_EXCHANGE, exchange.exchangeId(), settlementId, groupId,
                        exchange.debtorUserId(), exchange.creditorUserId(), exchange.exchangeValue(), exchange.status().name()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> exchange)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }
}
