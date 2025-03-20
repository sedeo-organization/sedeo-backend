package com.sedeo.friend.repository.db.mapper;

import com.sedeo.domain.friend.model.Friendship;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class FriendshipMapper implements RowMapper<Friendship> {

    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Friendship(
                (UUID) rs.getObject(Fields.FRIENDSHIP_ID),
                (UUID) rs.getObject(Fields.FIRST_USER_ID),
                (UUID) rs.getObject(Fields.SECOND_USER_ID)
        );
    }

    private static class Fields {
        private static final String FRIENDSHIP_ID = "friendship_id";
        private static final String FIRST_USER_ID = "first_user_id";
        private static final String SECOND_USER_ID = "second_user_id";
    }
}

