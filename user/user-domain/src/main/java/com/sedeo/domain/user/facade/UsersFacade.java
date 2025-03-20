package com.sedeo.domain.user.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.user.db.UserRepository;
import com.sedeo.event.UserCreatedSuccessfullyEvent;
import com.sedeo.domain.user.db.PasswordResetTokenRepository;
import com.sedeo.domain.user.model.PasswordResetToken;
import com.sedeo.domain.user.model.User;
import com.sedeo.domain.user.model.error.UserError.UserNotFoundError;
import io.vavr.control.Either;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UsersFacade implements Users {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UsersFacade(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository,
                       ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Either<GeneralError, User> fetchUser(UUID userId) {
        return userRepository.findUser(userId)
                .filterOrElse(Objects::nonNull, error -> new UserNotFoundError());
    }

    @Override
    public Either<GeneralError, User> fetchUser(String email) {
        return userRepository.findUser(email)
                .filterOrElse(Objects::nonNull, error -> new UserNotFoundError());
    }

    @Override
    public Either<GeneralError, List<User>> fetchUsers(List<UUID> userIds) {
        return userRepository.findUsers(userIds);
    }

    @Override
    @Transactional
    public Either<GeneralError, Void> addToUsersAccountBalance(UUID userId, BigDecimal positiveAmount) {
        BigDecimal forcedPositiveAmount = positiveAmount.abs();
        return this.fetchUser(userId)
                .map(user -> user.withAddedBalance(forcedPositiveAmount))
                .flatMap(userRepository::updateUser)
                .flatMap(result -> Either.right(null));
    }

    @Override
    @Transactional
    public Either<GeneralError, Void> subtractFromUsersAccountBalance(UUID userId, BigDecimal positiveAmount) {
        BigDecimal forcedPositiveAmount = positiveAmount.abs();
        return this.fetchUser(userId)
                .map(user -> user.withReducedBalance(forcedPositiveAmount))
                .flatMap(userRepository::updateUser)
                .flatMap(result -> Either.right(null));
    }

    @Override
    public Boolean userExists(String email, String phoneNumber) {
        return userRepository.userExists(email, phoneNumber);
    }

    @Override
    public Either<GeneralError, Void> createUser(User user) {
        return userRepository.createUser(user)
                .flatMap(result -> publishUserCreatedSuccessfullyEvent(user));
    }

    @Override
    public Either<GeneralError, PasswordResetToken> createPasswordResetToken(String email) {
        return this.fetchUser(email)
                .map(user -> PasswordResetToken.randomUnusedToken(user.userId(), user.firstName(), user.lastName(), user.email()))
                .flatMap(passwordResetTokenRepository::save);
    }

    @Override
    @Transactional
    public Either<GeneralError, User> changeUsersPassword(UUID token, String newPassword) {
        return passwordResetTokenRepository.find(token)
                .flatMap(PasswordResetToken::useToken)
                .flatMap(passwordResetTokenRepository::update)
                .flatMap(updatedToken -> userRepository.findUser(updatedToken.userId()))
                .map(user -> user.withNewPassword(newPassword))
                .flatMap(userRepository::updateUser);
    }

    private Either<GeneralError, Void> publishUserCreatedSuccessfullyEvent(User user) {
        applicationEventPublisher.publishEvent(new UserCreatedSuccessfullyEvent(this, new UserCreatedSuccessfullyEvent.CreatedUserModel(
                user.userId(),
                user.firstName(),
                user.lastName(),
                user.phoneNumber()))
        );

        return Either.right(null);
    }
}
