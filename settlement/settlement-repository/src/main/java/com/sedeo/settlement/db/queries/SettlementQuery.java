package com.sedeo.settlement.db.queries;

public class SettlementQuery {

    private static final String SETTLEMENT_TABLE = "settlement_settlement";

    public static final String SAVE_SETTLEMENT = "INSERT INTO %s VALUES(?, ?, ?, ?)"
            .formatted(SETTLEMENT_TABLE);

    public static final String SETTLEMENTS_BY_GROUP_ID = "SELECT * FROM %s WHERE group_id = ?"
            .formatted(SETTLEMENT_TABLE);
}
