package com.sedeo.user.db.model;

import java.math.BigDecimal;
import java.util.UUID;

public record UserEntity(
        UUID userId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String password,
        BigDecimal accountBalance
) {
}
