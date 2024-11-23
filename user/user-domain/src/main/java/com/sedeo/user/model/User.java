package com.sedeo.user.model;

import java.math.BigDecimal;
import java.util.UUID;

public record User(
        UUID userId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String password,
        BigDecimal accountBalance
) {

    public User withReducedBalance(BigDecimal positiveBalance) {
        return new User(this.userId, this.firstName, this.lastName, this.phoneNumber, this.email, this.password, this.accountBalance.subtract(positiveBalance));
    }

    public User withAddedBalance(BigDecimal positiveBalance) {
        return new User(this.userId, this.firstName, this.lastName, this.phoneNumber, this.email, this.password, this.accountBalance.add(positiveBalance));
    }

    public static User withZeroBalance(UUID userId, String firstName, String lastName, String phoneNumber, String email, String password) {
        return new User(userId, firstName, lastName, phoneNumber, email, password, BigDecimal.ZERO);
    }
}
