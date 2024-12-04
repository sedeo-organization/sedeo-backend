package com.sedeo.settlement.db.mapper;

import com.sedeo.settlement.model.view.SummaryExchange;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SummaryExchangeMapper implements RowMapper<SummaryExchange> {

    @Override
    public SummaryExchange mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SummaryExchange(
                (UUID) rs.getObject(Fields.GROUP_ID),
                (UUID) rs.getObject(Fields.DEBTOR_USER_ID),
                rs.getString(Fields.DEBTOR_FIRST_NAME),
                rs.getString(Fields.DEBTOR_LAST_NAME),
                (UUID) rs.getObject(Fields.CREDITOR_USER_ID),
                rs.getString(Fields.CREDITOR_FIRST_NAME),
                rs.getString(Fields.CREDITOR_LAST_NAME),
                rs.getBigDecimal(Fields.SUMMARISED_EXCHANGE_VALUE)
        );
    }

    private static class Fields {
        private static final String GROUP_ID = "group_id";
        private static final String DEBTOR_USER_ID = "debtor_user_id";
        private static final String DEBTOR_FIRST_NAME = "debtor_first_name";
        private static final String DEBTOR_LAST_NAME = "debtor_last_name";
        private static final String CREDITOR_USER_ID = "creditor_user_id";
        private static final String CREDITOR_FIRST_NAME = "creditor_first_name";
        private static final String CREDITOR_LAST_NAME = "creditor_last_name";
        private static final String SUMMARISED_EXCHANGE_VALUE = "summarised_exchanges_value";
    }
}
