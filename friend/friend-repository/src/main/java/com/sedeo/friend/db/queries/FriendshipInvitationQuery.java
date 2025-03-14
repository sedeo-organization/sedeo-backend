package com.sedeo.friend.db.queries;

public class FriendshipInvitationQuery {

    private static final String FRIENDSHIP_INVITATION_TABLE = "friend_friendship_invitation";

    public static final String SAVE_FRIENDSHIP_INVITATION = "INSERT INTO %s VALUES (?, ?, ?, ?)"
            .formatted(FRIENDSHIP_INVITATION_TABLE);

    public static final String FRIENDSHIP_INVITATION_BY_USER_IDS = "SELECT * FROM %s WHERE inviting_user_id = ? AND invited_user_id = ? AND invitation_status = ?"
            .formatted(FRIENDSHIP_INVITATION_TABLE);

    public static final String UPDATE_FRIENDSHIP_INVITATION = ("UPDATE %s SET inviting_user_id = ?, invited_user_id = ?, invitation_status = ? WHERE " +
            "inviting_user_id = ? AND invited_user_id = ?").formatted(FRIENDSHIP_INVITATION_TABLE);

    public static final String FRIENDSHIP_INVITATION_EXISTS_BY_IDS = "SELECT EXISTS (SELECT 1 FROM %s WHERE inviting_user_id = ? AND invited_user_id = ?)"
            .formatted(FRIENDSHIP_INVITATION_TABLE);

    public static final String INCOMING_FRIENDSHIP_INVITATIONS_BY_USER_ID = "SELECT * FROM %s WHERE invited_user_id = ? AND invitation_status = ?"
            .formatted(FRIENDSHIP_INVITATION_TABLE);

    public static final String FRIENDSHIP_INVITATIONS_BY_ID = "SELECT * FROM %s WHERE friendship_invitation_id = ? AND invitation_status = ?"
            .formatted(FRIENDSHIP_INVITATION_TABLE);
}
