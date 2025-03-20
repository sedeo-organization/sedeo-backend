package com.sedeo.user.repository.db.queries;

public class PasswordResetTokenQuery {

    private static final String PASSWORD_RESET_TOKEN_TABLE = "user_password_reset_token";

    public static final String SAVE_PASSWORD_RESET_TOKEN = "INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?)"
            .formatted(PASSWORD_RESET_TOKEN_TABLE);

    public static final String UPDATE_PASSWORD_RESET_TOKEN = ("UPDATE %s SET user_id = ?, first_name = ?, last_name = ?,"
            + " email = ?, expiration_time = ?, token_status = ? WHERE token = ?")
            .formatted(PASSWORD_RESET_TOKEN_TABLE);

    public static final String FIND_PASSWORD_RESET_TOKEN_BY_TOKEN_ID = "SELECT * FROM %s WHERE token = ?"
            .formatted(PASSWORD_RESET_TOKEN_TABLE);
}
