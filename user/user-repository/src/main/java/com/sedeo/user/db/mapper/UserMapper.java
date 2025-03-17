package com.sedeo.user.db.mapper;

import com.sedeo.user.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                (UUID) rs.getObject(Fields.USER_ID),
                rs.getString(Fields.FIRST_NAME),
                rs.getString(Fields.LAST_NAME),
                rs.getString(Fields.PHONE_NUMBER),
                rs.getString(Fields.EMAIL),
                rs.getString(Fields.PASSWORD),
                rs.getBigDecimal(Fields.ACCOUNT_BALANCE)
        );
    }

    private static class Fields {
        private static final String USER_ID = "user_id";
        private static final String FIRST_NAME = "first_name";
        private static final String LAST_NAME = "last_name";
        private static final String PHONE_NUMBER = "phone_number";
        private static final String EMAIL = "email";
        private static final String PASSWORD = "password";
        private static final String ACCOUNT_BALANCE = "account_balance";
    }
}
