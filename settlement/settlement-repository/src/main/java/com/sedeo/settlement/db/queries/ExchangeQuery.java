package com.sedeo.settlement.db.queries;

public record ExchangeQuery() {

    private static final String EXCHANGE_TABLE = "settlement_exchange";

    public static final String SAVE_EXCHANGE = "INSERT INTO %s VALUES(?, ?, ?, ?, ?, ?)"
            .formatted(EXCHANGE_TABLE);
}
