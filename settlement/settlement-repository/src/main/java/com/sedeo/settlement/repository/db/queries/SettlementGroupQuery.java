package com.sedeo.settlement.repository.db.queries;

public class SettlementGroupQuery {

    private static final String SETTLEMENT_GROUP_TABLE = "settlement_group";

    public static final String GROUP_IDS_PARAMETER = "groupIds";

    public static final String FIND_GROUPS_BY_GROUP_IDS = "SELECT * FROM %s WHERE group_id IN (:%s)"
            .formatted(SETTLEMENT_GROUP_TABLE, GROUP_IDS_PARAMETER);

    public static final String SAVE_SETTLEMENT_GROUP = "INSERT INTO %s VALUES(?, ?)"
            .formatted(SETTLEMENT_GROUP_TABLE);

    public static final String SETTLEMENT_GROUP_EXISTS_BY_GROUP_ID = "SELECT EXISTS (SELECT 1 FROM %s WHERE group_id = ?)"
            .formatted(SETTLEMENT_GROUP_TABLE);
}
