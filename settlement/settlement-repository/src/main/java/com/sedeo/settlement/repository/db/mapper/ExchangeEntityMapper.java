package com.sedeo.settlement.repository.db.mapper;

import com.sedeo.settlement.repository.db.model.ExchangeEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ExchangeEntityMapper implements RowMapper<ExchangeEntity> {

    @Override
    public ExchangeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ExchangeEntity(
                (UUID) rs.getObject(Fields.EXCHANGE_ID),
                (UUID) rs.getObject(Fields.SETTLEMENT_ID),
                (UUID) rs.getObject(Fields.GROUP_ID),
                (UUID) rs.getObject(Fields.DEBTOR_USER_ID),
                (UUID) rs.getObject(Fields.CREDITOR_USER_ID),
                rs.getBigDecimal(Fields.EXCHANGE_VALUE),
                rs.getString(Fields.STATUS)
        );
    }

    private static final class Fields {
        private static final String EXCHANGE_ID = "exchange_id";
        private static final String SETTLEMENT_ID = "settlement_id";
        private static final String GROUP_ID = "group_id";
        private static final String DEBTOR_USER_ID = "debtor_user_id";
        private static final String CREDITOR_USER_ID = "creditor_user_id";
        private static final String EXCHANGE_VALUE = "exchange_value";
        private static final String STATUS = "status";

    }
}
