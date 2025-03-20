package com.sedeo.friend.repository.db.mapper;

import com.sedeo.domain.friend.model.DetailedFriendshipInvitation;
import com.sedeo.domain.friend.model.Friend;
import com.sedeo.domain.friend.model.InvitationStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DetailedFriendshipInvitationMapper implements RowMapper<DetailedFriendshipInvitation> {

    @Override
    public DetailedFriendshipInvitation mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DetailedFriendshipInvitation(
                (UUID) rs.getObject(Fields.FRIENDSHIP_INVITATION_ID),
                new Friend(
                        (UUID) rs.getObject(Fields.INVITING_USER_ID),
                        rs.getString(Fields.INVITING_FIRST_NAME),
                        rs.getString(Fields.INVITING_LAST_NAME),
                        rs.getString(Fields.INVITING_PHONE_NUMBER)),
                new Friend(
                        (UUID) rs.getObject(Fields.INVITED_USER_ID),
                        rs.getString(Fields.INVITED_FIRST_NAME),
                        rs.getString(Fields.INVITED_LAST_NAME),
                        rs.getString(Fields.INVITED_PHONE_NUMBER)),
                InvitationStatus.valueOf(rs.getString(Fields.INVITATION_STATUS))
        );
    }

    private static final class Fields {
        private static final String FRIENDSHIP_INVITATION_ID = "friendship_invitation_id";
        private static final String INVITATION_STATUS = "invitation_status";

        private static final String INVITING_USER_ID = "inviting_user_id";
        private static final String INVITING_FIRST_NAME = "inviting_first_name";
        private static final String INVITING_LAST_NAME = "inviting_last_name";
        private static final String INVITING_PHONE_NUMBER = "inviting_phone_number";

        private static final String INVITED_USER_ID = "invited_user_id";
        private static final String INVITED_FIRST_NAME = "invited_first_name";
        private static final String INVITED_LAST_NAME = "invited_last_name";
        private static final String INVITED_PHONE_NUMBER = "invited_phone_number";
    }
}
