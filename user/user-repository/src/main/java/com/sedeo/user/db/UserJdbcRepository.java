package com.sedeo.user.db;

import com.sedeo.common.error.DatabaseError.DatabaseReadUnsuccessfulError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.mapper.UserMapper;
import com.sedeo.user.db.model.UserEntity;
import com.sedeo.user.db.queries.Query;
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

import static org.springframework.dao.support.DataAccessUtils.singleResult;

public class UserJdbcRepository implements UserRepository {

    private static final UserMapper USER_MAPPER = new UserMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserJdbcRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public Either<GeneralError, UserEntity> findUser(UUID userId) {
        return Try.of(() -> singleResult(jdbcTemplate.query(Query.USER_BY_ID, USER_MAPPER, userId)))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<UserEntity>> findUsers(List<UUID> userIds) {
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(Query.USER_IDS_PARAMETER, userIds));
        return Try.of(() -> namedParameterJdbcOperations.query(Query.USERS_BY_IDS, parameters, USER_MAPPER))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<UserEntity>> findUsersFriends(UUID userId) {
        return Try.of(() -> jdbcTemplate.query(Query.FRIENDS_BY_USER_IDS, USER_MAPPER, userId, userId, userId))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<UserEntity>> findFriendInvitationUsers(UUID userId) {
        return Try.of(() -> jdbcTemplate.query(Query.FRIEND_INVITATION_BY_USER_ID, USER_MAPPER, userId))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }
}
