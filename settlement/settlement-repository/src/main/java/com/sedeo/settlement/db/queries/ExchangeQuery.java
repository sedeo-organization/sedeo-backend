package com.sedeo.settlement.db.queries;

public record ExchangeQuery() {

    private static final String EXCHANGE_TABLE = "settlement_exchange";

    public static final String SAVE_EXCHANGE = "INSERT INTO %s VALUES(?, ?, ?, ?, ?, ?, ?)"
            .formatted(EXCHANGE_TABLE);

    public static final String EXCHANGES_BY_SETTLEMENT_ID = "SELECT * FROM %s WHERE settlement_id = ?"
            .formatted(EXCHANGE_TABLE);

    public static final String UPDATE_EXCHANGE = ("UPDATE %s SET settlement_id = ?, group_id = ?, debtor_user_id = ?, creditor_user_id = ?," +
            " exchange_value = ?, status = ? WHERE exchange_id = ?")
            .formatted(EXCHANGE_TABLE);

    public static final String EXCHANGES_BY_GROUP_ID_AND_USER_ID = ("SELECT * FROM %S WHERE group_id = ? AND (creditor_user_id = ? OR debtor_user_id = ?)")
            .formatted(EXCHANGE_TABLE);
}
