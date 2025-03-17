package com.sedeo.user.db;

import com.sedeo.common.error.DatabaseError.DatabaseReadUnsuccessfulError;
import com.sedeo.common.error.DatabaseError.DatabaseWriteUnsuccessfulError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.mapper.UserMapper;
import com.sedeo.user.db.queries.UserQuery;
import com.sedeo.user.model.User;
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

import static com.sedeo.user.db.queries.UserQuery.*;
import static java.lang.Boolean.TRUE;
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
    public Either<GeneralError, User> findUser(UUID userId) {
        return Try.of(() -> singleResult(jdbcTemplate.query(UserQuery.USER_BY_ID, USER_MAPPER, userId)))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<User>> findUsers(List<UUID> userIds) {
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(UserQuery.USER_IDS_PARAMETER, userIds));
        return Try.of(() -> namedParameterJdbcOperations.query(UserQuery.USERS_BY_IDS, parameters, USER_MAPPER))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, User> findUser(String email) {
        return Try.of(() -> singleResult(jdbcTemplate.query(USER_BY_EMAIL, USER_MAPPER, email)))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, User> updateUser(User user) {
        return Try.of(() -> jdbcTemplate.update(UPDATE_USER,
                        user.firstName(),
                        user.lastName(),
                        user.phoneNumber(),
                        user.email(),
                        user.password(),
                        user.accountBalance(),
                        user.userId()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> user)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Boolean userExists(String email, String phoneNumber) {
        return TRUE.equals(jdbcTemplate.queryForObject(UserQuery.USER_EXISTS_BY_EMAIL_OR_PHONE_NUMBER, Boolean.class, email, phoneNumber));
    }

    @Override
    public Either<GeneralError, User> createUser(User user) {
        return Try.of(() -> jdbcTemplate.update(SAVE_USER, user.userId(), user.firstName(), user.lastName(),
                        user.phoneNumber(), user.email(), user.password(), user.accountBalance()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> user)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }
}
