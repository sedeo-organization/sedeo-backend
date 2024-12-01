package com.sedeo.user.db.model;

import com.sedeo.user.model.TokenStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PasswordResetTokenEntity(UUID token, UUID userId, String firstName, String lastName, String email,
                                       LocalDateTime expirationTime, TokenStatus tokenStatus) {
}
