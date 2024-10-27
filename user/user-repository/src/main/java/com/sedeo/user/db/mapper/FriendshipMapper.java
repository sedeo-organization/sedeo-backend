package com.sedeo.user.db.mapper;

import com.sedeo.user.db.model.FriendshipEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class FriendshipMapper implements RowMapper<FriendshipEntity> {
    @Override
    public FriendshipEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FriendshipEntity(
                (UUID) rs.getObject(Fields.FIRST_USER_ID),
                (UUID) rs.getObject(Fields.SECOND_USER_ID)
        );
    }

    private static class Fields {
        private static final String FIRST_USER_ID = "first_user_id";
        private static final String SECOND_USER_ID = "second_user_id";
    }
}
