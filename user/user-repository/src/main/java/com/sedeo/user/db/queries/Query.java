package com.sedeo.user.db.queries;

public class Query {

    private static final String USER_TABLE = "user_user";

    public static final String USER_BY_ID = "SELECT * FROM %s WHERE user_id = ?"
            .formatted(USER_TABLE);
}
