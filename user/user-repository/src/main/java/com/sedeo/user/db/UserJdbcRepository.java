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

import java.util.UUID;

import static org.springframework.dao.support.DataAccessUtils.singleResult;

public class UserJdbcRepository implements UserRepository {

    private static final UserMapper USER_MAPPER = new UserMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserJdbcRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Either<GeneralError, UserEntity> find(UUID userId) {
        return Try.of(() -> singleResult(jdbcTemplate.query(Query.USER_BY_ID, USER_MAPPER, userId)))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseReadUnsuccessfulError::new);
    }
}
