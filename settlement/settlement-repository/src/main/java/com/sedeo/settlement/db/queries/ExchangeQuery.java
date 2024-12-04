package com.sedeo.settlement.db.queries;

public record ExchangeQuery() {

    private static final String EXCHANGE_TABLE = "settlement_exchange";

    public static final String STATUSES_PARAMETER = "statuses";
    public static final String GROUP_ID_PARAMETER = "group_id";
    public static final String CREDITOR_USER_ID_PARAMETER = "creditorUserId";
    public static final String DEBTOR_USER_ID_PARAMETER = "debtorUserId";

    public static final String SAVE_EXCHANGE = "INSERT INTO %s VALUES(?, ?, ?, ?, ?, ?, ?)"
            .formatted(EXCHANGE_TABLE);

    public static final String EXCHANGES_BY_SETTLEMENT_ID = "SELECT * FROM %s WHERE settlement_id = ?"
            .formatted(EXCHANGE_TABLE);

    public static final String UPDATE_EXCHANGE = ("UPDATE %s SET settlement_id = ?, group_id = ?, debtor_user_id = ?, creditor_user_id = ?," +
            " exchange_value = ?, status = ? WHERE exchange_id = ?")
            .formatted(EXCHANGE_TABLE);

    public static final String UPDATE_EXCHANGE_STATUS = ("UPDATE %s SET status = ? WHERE exchange_id = ?")
            .formatted(EXCHANGE_TABLE);

    public static final String EXCHANGES_BY_GROUP_ID_AND_USER_ID = ("SELECT * FROM %s WHERE group_id = ? AND (creditor_user_id = ? OR debtor_user_id = ?)")
            .formatted(EXCHANGE_TABLE);

    public static final String EXCHANGES_BY_GROUP_ID_AND_USER_IDS = ("SELECT * FROM %s WHERE group_id = :%s AND (creditor_user_id = :%s AND debtor_user_id = :%s) OR (creditor_user_id = :%s AND debtor_user_id = :%s) " +
            "AND status IN (:%s)")
            .formatted(EXCHANGE_TABLE, GROUP_ID_PARAMETER, CREDITOR_USER_ID_PARAMETER, DEBTOR_USER_ID_PARAMETER, DEBTOR_USER_ID_PARAMETER, CREDITOR_USER_ID_PARAMETER, STATUSES_PARAMETER);

    public static final String AGGREGATE_EXCHANGES_SUMMARY = ("""
                WITH PairwiseSum AS (
                SELECT
                    se.group_id,
                    LEAST(se.debtor_user_id, se.creditor_user_id) AS user1,
                    GREATEST(se.debtor_user_id, se.creditor_user_id) AS user2,
                    SUM(CASE\s
                        WHEN se.debtor_user_id < se.creditor_user_id THEN se.exchange_value\s
                        ELSE -se.exchange_value\s
                    END) AS net_sum
                FROM
                    %s se
                WHERE
                    se.status IN (:%s) AND se.group_id = :%s
                GROUP BY
                    se.group_id, LEAST(se.debtor_user_id, se.creditor_user_id), GREATEST(se.debtor_user_id, se.creditor_user_id)
            ),
            SummedExchange AS (
                SELECT
                    ps.group_id,
                    SUM(ps.net_sum) AS total_net_sum,
                    CASE\s
                        WHEN SUM(ps.net_sum) >= 0 THEN ps.user1
                        ELSE ps.user2
                    END AS debtor_user_id,
                    CASE\s
                        WHEN SUM(ps.net_sum) >= 0 THEN ps.user2
                        ELSE ps.user1
                    END AS creditor_user_id
                FROM
                    PairwiseSum ps
                GROUP BY
                    ps.group_id, ps.user1, ps.user2
            )
            SELECT
                se.group_id,
                se.debtor_user_id,
                p1.first_name AS debtor_first_name,
                p1.last_name AS debtor_last_name,
                se.creditor_user_id,
                p2.first_name AS creditor_first_name,
                p2.last_name AS creditor_last_name,
                ABS(se.total_net_sum) AS summarised_exchanges_value
            FROM
                SummedExchange se
            JOIN
                settlement_participant p1 ON se.group_id = p1.group_id AND se.debtor_user_id = p1.user_id
            JOIN
                settlement_participant p2 ON se.group_id = p2.group_id AND se.creditor_user_id = p2.user_id
            """).formatted(EXCHANGE_TABLE, STATUSES_PARAMETER, GROUP_ID_PARAMETER);
}
