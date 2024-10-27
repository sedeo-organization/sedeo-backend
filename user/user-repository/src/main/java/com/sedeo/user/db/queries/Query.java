package com.sedeo.user.db.queries;

public class Query {

    private static final String USER_TABLE = "user_user";
    private static final String FRIENDSHIP_TABLE = "user_friendship";
    private static final String FRIEND_INVITATION_TABLE = "user_friend_invitation";
    public static final String USER_IDS_PARAMETER = "ids";

    public static final String USER_BY_ID = "SELECT * FROM %s WHERE user_id = ?"
            .formatted(USER_TABLE);
    public static final String USERS_BY_IDS = "SELECT * FROM %s WHERE user_id IN (:%s)"
            .formatted(USER_TABLE, USER_IDS_PARAMETER);

    public static final String FRIENDS_BY_USER_IDS = ("SELECT u.* FROM %s u JOIN %s f ON (" +
            "(f.first_user_id = u.user_id AND f.second_user_id = ?) OR " +
            "(f.second_user_id = u.user_id AND f.first_user_id = ?)) " +
            "WHERE u.user_id != ?")
                    .formatted(USER_TABLE, FRIENDSHIP_TABLE);
    public static final String FRIEND_INVITATION_BY_USER_ID = ("SELECT * FROM %s JOIN %s ON user_id = ?")
            .formatted(USER_TABLE, FRIEND_INVITATION_TABLE);
}
