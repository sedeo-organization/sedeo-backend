package com.sedeo.settlement.repository.db;

import com.sedeo.common.error.DatabaseError;
import com.sedeo.common.error.DatabaseError.DatabaseWriteUnsuccessfulError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.settlement.db.ExchangeRepository;
import com.sedeo.settlement.repository.db.mapper.ExchangeEntityMapper;
import com.sedeo.settlement.repository.db.mapper.SummaryExchangeMapper;
import com.sedeo.settlement.repository.db.modelmapper.ExchangeMapper;
import com.sedeo.domain.settlement.model.Exchange;
import com.sedeo.domain.settlement.model.ExchangeStatus;
import com.sedeo.domain.settlement.model.view.SummaryExchange;
import io.vavr.collection.Traversable;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.sedeo.settlement.repository.db.queries.ExchangeQuery.*;
import static com.sedeo.settlement.repository.db.queries.ParticipantQuery.STATUSES_PARAMETER;

public class ExchangeJdbcRepository implements ExchangeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeJdbcRepository.class);
    private static final ExchangeEntityMapper EXCHANGE_ENTITY_MAPPER = new ExchangeEntityMapper();
    private static final SummaryExchangeMapper SUMMARY_EXCHANGE_MAPPER = new SummaryExchangeMapper();
    private static final ExchangeMapper EXCHANGE_MAPPER = ExchangeMapper.INSTANCE;

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ExchangeJdbcRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

    @Override
    public Either<GeneralError, List<Exchange>> updateExchangesStatus(List<Exchange> exchanges) {
        List<Object[]> batchedExchanges = new ArrayList<>();
        for (Exchange exchange : exchanges) {
            Object[] values = new Object[] {
                    exchange.status().name(), exchange.exchangeId()};
            batchedExchanges.add(values);
        }
        return Try.of(() -> jdbcTemplate.batchUpdate(UPDATE_EXCHANGE_STATUS, batchedExchanges))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> exchanges)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<Exchange>> findExchangesInvolvingParticipant(UUID groupId, UUID participantId) {
        return Try.of(() -> jdbcTemplate.query(EXCHANGES_BY_GROUP_ID_AND_USER_ID, EXCHANGE_ENTITY_MAPPER, groupId, participantId, participantId))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .map(EXCHANGE_MAPPER::exchangeEntityListToExchangeList)
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<Exchange>> findExchangesInvolvingParticipantsWithinGroupByStatuses(UUID groupId,
                                                                                                        UUID firstParticipantId,
                                                                                                        UUID secondParticipantId,
                                                                                                        List<ExchangeStatus> statuses) {
        List<String> exchangeStatuses = statuses.stream().map(Enum::toString).toList();
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
                GROUP_ID_PARAMETER, groupId,
                CREDITOR_USER_ID_PARAMETER, firstParticipantId,
                DEBTOR_USER_ID_PARAMETER, secondParticipantId,
                STATUSES_PARAMETER, exchangeStatuses));

        return Try.of(() -> namedParameterJdbcTemplate.query(EXCHANGES_BY_GROUP_ID_AND_USER_IDS, parameters, EXCHANGE_ENTITY_MAPPER))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .map(EXCHANGE_MAPPER::exchangeEntityListToExchangeList)
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<SummaryExchange>> aggregateExchangesGroupSummary(UUID groupId, List<ExchangeStatus> statuses) {
        List<String> exchangeStatuses = statuses.stream().map(Enum::toString).toList();
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
                GROUP_ID_PARAMETER, groupId,
                STATUSES_PARAMETER, exchangeStatuses));

        return Try.of(() -> namedParameterJdbcTemplate.query(AGGREGATE_EXCHANGES_SUMMARY, parameters, SUMMARY_EXCHANGE_MAPPER))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
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
