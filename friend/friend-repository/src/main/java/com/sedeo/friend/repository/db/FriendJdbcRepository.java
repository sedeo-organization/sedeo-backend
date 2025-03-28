package com.sedeo.friend.repository.db;

import com.sedeo.common.error.DatabaseError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.friend.db.FriendRepository;
import com.sedeo.friend.repository.db.mapper.FriendMapper;
import com.sedeo.domain.friend.model.Friend;
import com.sedeo.friend.repository.db.queries.FriendQuery;
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

import static java.lang.Boolean.TRUE;

public class FriendJdbcRepository implements FriendRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendJdbcRepository.class);
    private static final FriendMapper FRIEND_MAPPER = new FriendMapper();

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public FriendJdbcRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Either<GeneralError, Friend> save(Friend friend) {
        return Try.of(() -> jdbcTemplate.update(FriendQuery.SAVE_FRIEND, friend.userId(), friend.firstName(), friend.lastName(), friend.phoneNumber()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> friend)
                .mapLeft(DatabaseError.DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public boolean friendExistsById(UUID userId) {
        return TRUE.equals(jdbcTemplate.queryForObject(FriendQuery.FRIEND_EXISTS_BY_ID, Boolean.class, userId));
    }

    @Override
    public Either<GeneralError, List<Friend>> findFriendsByIds(List<UUID> userIds) {
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(FriendQuery.USER_IDS_PARAMETER, userIds));
        return Try.of(() -> namedParameterJdbcOperations.query(FriendQuery.FIND_FRIENDS_BY_IDS, parameters, FRIEND_MAPPER))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<Friend>> findUsersPotentialFriends(UUID userId, String searchPhrase) {
        String concatenatedSearchPhrase = "%" + searchPhrase + "%";
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(FriendQuery.CURRENT_USER_ID_PARAMETER, userId,
                FriendQuery.SEARCH_PHRASE_PARAMETER, concatenatedSearchPhrase));
        return Try.of(() -> namedParameterJdbcOperations.query(FriendQuery.POTENTIAL_FRIENDS_BY_SEARCH_PHRASE, parameters, FRIEND_MAPPER))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }
}
