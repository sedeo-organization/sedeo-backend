package com.sedeo.friend.repository.db.queries;

public class FriendshipQuery {

    private static final String FRIENDSHIP_TABLE = "friend_friendship";

    public static final String FIND_FRIENDSHIPS_BY_USER_ID = "SELECT * FROM %s WHERE first_user_id = ? OR second_user_id = ?"
            .formatted(FRIENDSHIP_TABLE);

    public static final String FRIENDSHIP_EXISTS_BY_IDS = ("SELECT EXISTS (SELECT 1 FROM %s f WHERE " +
            "(f.first_user_id = ? AND f.second_user_id = ?) OR (f.second_user_id = ? AND f.first_user_id = ?))")
            .formatted(FRIENDSHIP_TABLE);

    public static final String SAVE_FRIENDSHIP = "INSERT INTO %s VALUES (?, ?, ?)".formatted(FRIENDSHIP_TABLE);
}
