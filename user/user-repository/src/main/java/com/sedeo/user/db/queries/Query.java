package com.sedeo.user.db.queries;

public class Query {

    private static final String USER_TABLE = "user_user";
    private static final String FRIENDSHIP_TABLE = "user_friendship";
    private static final String FRIEND_INVITATION_TABLE = "user_friend_invitation";
    public static final String USER_IDS_PARAMETER = "ids";
    public static final String SEARCH_PHRASE_PARAMETER = "searchPhrase";
    public static final String CURRENT_USER_ID_PARAMETER = "currentUserId";

    public static final String USER_BY_ID = "SELECT * FROM %s WHERE user_id = ?"
            .formatted(USER_TABLE);

    public static final String USER_BY_EMAIL = "SELECT * FROM %s WHERE email = ?"
            .formatted(USER_TABLE);

    public static final String USERS_BY_IDS = "SELECT * FROM %s WHERE user_id IN (:%s)"
            .formatted(USER_TABLE, USER_IDS_PARAMETER);

    public static final String FRIENDS_BY_USER_IDS = ("SELECT u.* FROM %s u JOIN %s f ON (" +
            "(f.first_user_id = u.user_id AND f.second_user_id = ?) OR " +
            "(f.second_user_id = u.user_id AND f.first_user_id = ?)) " +
            "WHERE u.user_id != ?")
                    .formatted(USER_TABLE, FRIENDSHIP_TABLE);

    public static final String FRIEND_INVITATIONS_BY_USER_ID = ("SELECT * FROM %s JOIN %s ON user_id = inviting_user_id WHERE requested_user_id = ? AND invitation_status = ?")
            .formatted(USER_TABLE, FRIEND_INVITATION_TABLE);

    public static final String POTENTIAL_FRIENDS_BY_SEARCH_PHRASE = ("WITH friends AS (" +
            "    SELECT " +
            "        CASE " +
            "            WHEN first_user_id = :currentUserId THEN second_user_id" +
            "            ELSE first_user_id " +
            "        END AS friend_id " +
            "    FROM %s " +
            "    WHERE :currentUserId IN (first_user_id, second_user_id) " +
            ") " +
            "SELECT * " +
            "FROM %s u " +
            "WHERE " +
            "    (u.phone_number LIKE :searchPhrase " +
            "     OR u.first_name LIKE :searchPhrase " +
            "     OR u.last_name LIKE :searchPhrase) " +
            "    AND u.user_id != :currentUserId " +
            "    AND u.user_id NOT IN (SELECT friend_id FROM friends);").formatted(FRIENDSHIP_TABLE, USER_TABLE);

    public static final String FRIENDS_EXIST_BY_IDS = ("SELECT EXISTS (SELECT 1 FROM %s f WHERE " +
            "(f.first_user_id = ? AND f.second_user_id = ?) OR (f.second_user_id = ? AND f.first_user_id = ?))")
            .formatted(FRIENDSHIP_TABLE);

    public static final String FRIEND_INVITATION_BY_USER_IDS = "SELECT * FROM %s WHERE inviting_user_id = ? AND requested_user_id = ? AND invitation_status = ?"
            .formatted(FRIEND_INVITATION_TABLE);

    public static final String SAVE_FRIEND_INVITATION = "INSERT INTO %s VALUES (?, ?, ?)"
            .formatted(FRIEND_INVITATION_TABLE);

    public static final String FRIEND_INVITATION_EXISTS_BY_IDS = "SELECT EXISTS (SELECT 1 FROM %s WHERE inviting_user_id = ? AND requested_user_id = ?)";

    public static final String UPDATE_FRIEND_INVITATION = ("UPDATE %s SET inviting_user_id = ?, requested_user_id = ?, invitation_status = ? WHERE " +
            "inviting_user_id = ? AND requested_user_id = ?").formatted(FRIEND_INVITATION_TABLE);

    public static final String SAVE_FRIENDSHIP = "INSERT INTO %s VALUES (?, ?)".formatted(FRIENDSHIP_TABLE);

    public static final String UPDATE_USER = ("UPDATE %s SET first_name = ?, last_name = ?, phone_number = ?, email = ?," +
            " password = ?, account_balance = ? WHERE user_id = ?").formatted(USER_TABLE);

    public static final String USER_EXISTS_BY_EMAIL_OR_PHONE_NUMBER = "SELECT EXISTS (SELECT 1 FROM %s WHERE email = ? OR phone_number = ?)"
            .formatted(USER_TABLE);

    public static final String SAVE_USER = "INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, ?)"
            .formatted(USER_TABLE);
}
