package com.sedeo.friend.repository.db.mapper;

import com.sedeo.domain.friend.model.Friend;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class FriendMapper implements RowMapper<Friend> {

    @Override
    public Friend mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Friend(
                (UUID) rs.getObject(Fields.USER_ID),
                rs.getString(Fields.FIRST_NAME),
                rs.getString(Fields.LAST_NAME),
                rs.getString(Fields.PHONE_NUMBER)
        );
    }

    private static final class Fields {
        private static final String USER_ID = "user_id";
        private static final String FIRST_NAME = "first_name";
        private static final String LAST_NAME = "last_name";
        private static final String PHONE_NUMBER = "phone_number";
    }
}
