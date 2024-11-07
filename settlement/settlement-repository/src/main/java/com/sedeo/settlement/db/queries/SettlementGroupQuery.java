package com.sedeo.settlement.db.queries;

public class SettlementGroupQuery {

    private static final String SETTLEMENT_GROUP_TABLE = "settlement_group";

    public static final String GROUP_IDS_PARAMETER = "groupIds";

    public static final String FIND_GROUPS_BY_GROUP_IDS = "SELECT * FROM %s WHERE group_id IN (:%s)"
            .formatted(SETTLEMENT_GROUP_TABLE, GROUP_IDS_PARAMETER);
}
