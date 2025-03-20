package com.sedeo.settlement.repository.db.mapper;

import com.sedeo.settlement.repository.db.model.SettlementGroupEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SettlementGroupMapper implements RowMapper<SettlementGroupEntity> {

    @Override
    public SettlementGroupEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SettlementGroupEntity(
                (UUID) rs.getObject(Fields.GROUP_ID),
                rs.getString(Fields.TITLE)
        );
    }

    private static class Fields {
        private static final String GROUP_ID = "group_id";
        private static final String TITLE = "title";
    }
}
