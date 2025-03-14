package com.sedeo.user.db;

import com.sedeo.common.error.DatabaseError;
import com.sedeo.common.error.GeneralError;
import com.sedeo.user.db.mapper.PasswordResetTokenMapper;
import com.sedeo.user.db.model.PasswordResetTokenEntity;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static com.sedeo.user.db.queries.PasswordResetTokenQuery.*;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

public class PasswordResetTokenJdbcRepository implements PasswordResetTokenRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserJdbcRepository.class);
    private static final PasswordResetTokenMapper PASSWORD_RESET_TOKEN_MAPPER = new PasswordResetTokenMapper();

    private final JdbcTemplate jdbcTemplate;

    public PasswordResetTokenJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Either<GeneralError, PasswordResetTokenEntity> save(PasswordResetTokenEntity passwordResetToken) {
        return Try.of(() -> jdbcTemplate.update(SAVE_PASSWORD_RESET_TOKEN,
                        passwordResetToken.token(),
                        passwordResetToken.userId(),
                        passwordResetToken.firstName(),
                        passwordResetToken.lastName(),
                        passwordResetToken.email(),
                        passwordResetToken.expirationTime(),
                        passwordResetToken.tokenStatus().name()))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .map(result -> passwordResetToken)
                .mapLeft(DatabaseError.DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, PasswordResetTokenEntity> update(PasswordResetTokenEntity passwordResetToken) {
        return Try.of(() -> jdbcTemplate.update(UPDATE_PASSWORD_RESET_TOKEN,
                        passwordResetToken.userId(),
                        passwordResetToken.firstName(),
                        passwordResetToken.lastName(),
                        passwordResetToken.email(),
                        passwordResetToken.expirationTime(),
                        passwordResetToken.tokenStatus().name(),
                        passwordResetToken.token()))
                .onFailure(exception -> LOGGER.error("Database write error occurred", exception))
                .toEither()
                .map(result -> passwordResetToken)
                .mapLeft(DatabaseError.DatabaseWriteUnsuccessfulError::new);
    }

    @Override
    public Either<GeneralError, PasswordResetTokenEntity> find(UUID token) {
        return Try.of(() -> singleResult(jdbcTemplate.query(FIND_PASSWORD_RESET_TOKEN_BY_TOKEN_ID, PASSWORD_RESET_TOKEN_MAPPER, token)))
                .onFailure(exception -> LOGGER.error("Database read error occurred", exception))
                .toEither()
                .mapLeft(DatabaseError.DatabaseReadUnsuccessfulError::new);
    }
}
