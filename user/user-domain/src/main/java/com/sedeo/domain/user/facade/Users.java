package com.sedeo.domain.user.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.user.model.*;
import io.vavr.control.Either;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface Users {

    Either<GeneralError, User> fetchUser(UUID userId);

    Either<GeneralError, User> fetchUser(String email);

    Either<GeneralError, List<User>> fetchUsers(List<UUID> userIds);

    Either<GeneralError, Void> addToUsersAccountBalance(UUID userId, BigDecimal positiveAmount);

    Either<GeneralError, Void> subtractFromUsersAccountBalance(UUID userId, BigDecimal positiveAmount);

    Boolean userExists(String email, String phoneNumber);

    Either<GeneralError, Void> createUser(User user);

    Either<GeneralError, PasswordResetToken> createPasswordResetToken(String email);

    Either<GeneralError, User> changeUsersPassword(UUID token, String newPassword);
}
