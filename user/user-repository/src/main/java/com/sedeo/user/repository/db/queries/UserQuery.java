package com.sedeo.user.repository.db.queries;

public class UserQuery {

    private static final String USER_TABLE = "user_user";
    public static final String USER_IDS_PARAMETER = "ids";

    public static final String USER_BY_ID = "SELECT * FROM %s WHERE user_id = ? FOR UPDATE"
            .formatted(USER_TABLE);

    public static final String USER_BY_EMAIL = "SELECT * FROM %s WHERE email = ?"
            .formatted(USER_TABLE);

    public static final String USERS_BY_IDS = "SELECT * FROM %s WHERE user_id IN (:%s)"
            .formatted(USER_TABLE, USER_IDS_PARAMETER);

    public static final String UPDATE_USER = ("UPDATE %s SET first_name = ?, last_name = ?, phone_number = ?, email = ?,"
            + " password = ?, account_balance = ? WHERE user_id = ?").formatted(USER_TABLE);

    public static final String USER_EXISTS_BY_EMAIL_OR_PHONE_NUMBER = "SELECT EXISTS (SELECT 1 FROM %s WHERE email = ? OR phone_number = ?)"
            .formatted(USER_TABLE);

    public static final String SAVE_USER = "INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?)"
            .formatted(USER_TABLE);
}
