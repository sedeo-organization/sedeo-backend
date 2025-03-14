package com.sedeo.friend.db.queries;

public class FriendQuery {

    private static final String FRIEND_TABLE_NAME = "friend_friend";
    private static final String FRIENDSHIP_TABLE_NAME = "friend_friendship";
    public static final String USER_IDS_PARAMETER = "userIds";
    public static final String SEARCH_PHRASE_PARAMETER = "searchPhrase";
    public static final String CURRENT_USER_ID_PARAMETER = "currentUserId";

    public static final String SAVE_FRIEND = "INSERT INTO %s VALUES (?, ?, ?, ?)".formatted(FRIEND_TABLE_NAME);

    public static final String FRIEND_EXISTS_BY_ID = "SELECT EXISTS (SELECT 1 FROM %s WHERE user_id = ?)".formatted(FRIEND_TABLE_NAME);

    public static final String FIND_FRIENDS_BY_IDS = "SELECT * FROM %s WHERE user_id IN (:%s)".formatted(FRIEND_TABLE_NAME, USER_IDS_PARAMETER);

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
            "    AND u.user_id NOT IN (SELECT friend_id FROM friends);").formatted(FRIENDSHIP_TABLE_NAME, FRIEND_TABLE_NAME);
}
