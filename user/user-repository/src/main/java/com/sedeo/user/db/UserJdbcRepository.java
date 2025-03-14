package com.sedeo.user.db;

import com.sedeo.common.error.DatabaseError.DatabaseReadUnsuccessfulError;
import com.sedeo.common.error.DatabaseError.DatabaseWriteUnsuccessfulError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.mapper.UserMapper;
import com.sedeo.user.db.model.UserEntity;
import com.sedeo.user.db.queries.UserQuery;
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
    public Either<GeneralError, UserEntity> findUser(UUID userId) {
        return Try.of(() -> singleResult(jdbcTemplate.query(UserQuery.USER_BY_ID, USER_MAPPER, userId)))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, List<UserEntity>> findUsers(List<UUID> userIds) {
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(UserQuery.USER_IDS_PARAMETER, userIds));
        return Try.of(() -> namedParameterJdbcOperations.query(UserQuery.USERS_BY_IDS, parameters, USER_MAPPER))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, UserEntity> findUser(String email) {
        return Try.of(() -> singleResult(jdbcTemplate.query(USER_BY_EMAIL, USER_MAPPER, email)))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, UserEntity> updateUser(UserEntity userEntity) {
        return Try.of(() -> jdbcTemplate.update(UPDATE_USER,
                        userEntity.firstName(),
                        userEntity.lastName(),
                        userEntity.phoneNumber(),
                        userEntity.email(),
                        userEntity.password(),
                        userEntity.accountBalance(),
                        userEntity.userId()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> userEntity)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Boolean userExists(String email, String phoneNumber) {
        return TRUE.equals(jdbcTemplate.queryForObject(UserQuery.USER_EXISTS_BY_EMAIL_OR_PHONE_NUMBER, Boolean.class, email, phoneNumber));
    }

    @Override
    public Either<GeneralError, UserEntity> createUser(UserEntity userEntity) {
        return Try.of(() -> jdbcTemplate.update(SAVE_USER, userEntity.userId(), userEntity.firstName(), userEntity.lastName(),
                        userEntity.phoneNumber(), userEntity.email(), userEntity.password(), userEntity.accountBalance()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> userEntity)
                .mapLeft(DatabaseWriteUnsuccessfulError::new);
    }
}
