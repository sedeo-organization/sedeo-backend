package com.sedeo.friend.repository.db.queries;

public class DetailedFriendshipInvitationQuery {

    private static final String FRIEND_TABLE_NAME = "friend_friend";
    private static final String FRIENDSHIP_INVITATION_TABLE_NAME = "friend_friendship_invitation";

    public static final String FIND_DETAILED_FRIENDSHIP_INVITATIONS = """
            SELECT
                fi.*,
                iu.user_id AS inviting_user_id, iu.first_name AS inviting_first_name, iu.last_name AS inviting_last_name,
                iu.phone_number AS inviting_phone_number,
                ru.user_id AS invited_user_id, ru.first_name AS invited_first_name, ru.last_name AS invited_last_name,
                ru.phone_number AS invited_phone_number
            FROM %s fi
            JOIN %s iu ON fi.inviting_user_id = iu.user_id
            JOIN %s ru ON fi.invited_user_id = ru.user_id
            WHERE fi.invited_user_id = ? AND fi.invitation_status = ?;
            """.formatted(FRIENDSHIP_INVITATION_TABLE_NAME, FRIEND_TABLE_NAME, FRIEND_TABLE_NAME);

}
