package com.sedeo.friend.db.mapper;

import com.sedeo.friend.model.FriendshipInvitation;
import com.sedeo.friend.model.InvitationStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class FriendshipInvitationMapper implements RowMapper<FriendshipInvitation> {
    @Override
    public FriendshipInvitation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FriendshipInvitation(
                (UUID) rs.getObject(Fields.FRIENDSHIP_INVITATION_ID),
                (UUID) rs.getObject(Fields.INVITING_USER_ID),
                (UUID) rs.getObject(Fields.INVITED_USER_ID),
                InvitationStatus.valueOf(rs.getString(Fields.INVITATION_STATUS))
        );
    }

    private static class Fields {
        private static final String FRIENDSHIP_INVITATION_ID = "friendship_invitation_id";
        private static final String INVITING_USER_ID = "inviting_user_id";
        private static final String INVITED_USER_ID = "invited_user_id";
        private static final String INVITATION_STATUS = "invitation_status";
    }
}