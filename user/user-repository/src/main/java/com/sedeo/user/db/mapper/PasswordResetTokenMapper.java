package com.sedeo.user.db.mapper;

import com.sedeo.user.db.model.PasswordResetTokenEntity;
import com.sedeo.user.model.TokenStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PasswordResetTokenMapper implements RowMapper<PasswordResetTokenEntity> {

    @Override
    public PasswordResetTokenEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PasswordResetTokenEntity(
                (UUID) rs.getObject(Fields.TOKEN),
                (UUID) rs.getObject(Fields.USER_ID),
                rs.getString(Fields.FIRST_NAME),
                rs.getString(Fields.LAST_NAME),
                rs.getString(Fields.EMAIL),
                rs.getTimestamp(Fields.EXPIRATION_TIME).toLocalDateTime(),
                TokenStatus.valueOf(rs.getString(Fields.TOKEN_STATUS))
        );
    }

    private static class Fields {
        private static final String TOKEN = "token";
        private static final String USER_ID = "user_id";
        private static final String FIRST_NAME = "first_name";
        private static final String LAST_NAME = "last_name";
        private static final String EMAIL = "email";
        private static final String EXPIRATION_TIME = "expiration_time";
        private static final String TOKEN_STATUS = "token_status";
    }
}
