package com.sedeo.settlement.db;

import com.sedeo.common.error.DatabaseError.DatabaseWriteUnsuccessfulError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.Exchange;
import io.vavr.collection.Traversable;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

import static com.sedeo.settlement.db.queries.ExchangeQuery.SAVE_EXCHANGE;

public class ExchangeJdbcRepository implements ExchangeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeJdbcRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public ExchangeJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Either<GeneralError, List<Exchange>> save(List<Exchange> exchanges, UUID groupId, UUID settlementId) {
        return Either.sequence(exchanges.stream().map(exchange -> this.save(exchange, groupId, settlementId)).toList())
                .map(savedExchanges -> exchanges)
                .mapLeft(Traversable::head);
    }

    private Either<DatabaseWriteUnsuccessfulError, Exchange> save(Exchange exchange, UUID groupId, UUID settlementId) {
        return Try.of(() -> jdbcTemplate.update(SAVE_EXCHANGE, exchange.exchangeId(), settlementId, groupId, exchange.debtorUserId(), exchange.creditorUserId(), exchange.value()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> exchange)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }
}
