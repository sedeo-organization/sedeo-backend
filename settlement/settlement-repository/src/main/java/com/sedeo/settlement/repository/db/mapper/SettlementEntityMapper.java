package com.sedeo.settlement.repository.db.mapper;

import com.sedeo.settlement.repository.db.model.SettlementEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SettlementEntityMapper implements RowMapper<SettlementEntity> {

    @Override
    public SettlementEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SettlementEntity(
                (UUID) rs.getObject(Fields.SETTLEMENT_ID),
                (UUID) rs.getObject(Fields.GROUP_ID),
                rs.getString(Fields.TITLE),
                rs.getBigDecimal(Fields.TOTAL_VALUE)
        );
    }

    private static class Fields {
        private static final String SETTLEMENT_ID = "settlement_id";
        private static final String GROUP_ID = "group_id";
        private static final String TITLE = "title";
        private static final String TOTAL_VALUE = "total_value";

    }
}