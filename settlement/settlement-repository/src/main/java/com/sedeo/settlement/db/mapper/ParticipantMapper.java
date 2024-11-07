package com.sedeo.settlement.db.mapper;

import com.sedeo.settlement.db.model.ParticipantEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ParticipantMapper implements RowMapper<ParticipantEntity>  {

    @Override
    public ParticipantEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ParticipantEntity(
                (UUID) rs.getObject(Fields.GROUP_ID),
                (UUID) rs.getObject(Fields.USER_ID),
                rs.getString(Fields.SETTLEMENT_STATUS)
        );
    }

    private static class Fields {
        private static final String GROUP_ID = "group_id";
        private static final String USER_ID = "user_id";
        private static final String SETTLEMENT_STATUS = "settlement_status";
    }
}
