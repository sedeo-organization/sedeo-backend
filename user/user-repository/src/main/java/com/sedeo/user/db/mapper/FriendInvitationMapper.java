package com.sedeo.user.db.mapper;

import com.sedeo.user.db.model.FriendInvitationEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class FriendInvitationMapper implements RowMapper<FriendInvitationEntity> {
    @Override
    public FriendInvitationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FriendInvitationEntity(
                (UUID) rs.getObject(Fields.INVITING_USER_ID),
                (UUID) rs.getObject(Fields.REQUESTED_USER_ID),
                FriendInvitationEntity.InvitationStatus.valueOf(rs.getString(Fields.INVITATION_STATUS))
        );
    }

    private static class Fields {
        private static final String INVITING_USER_ID = "inviting_user_id";
        private static final String REQUESTED_USER_ID = "requested_user_id";
        private static final String INVITATION_STATUS = "invitation_status";
    }
}