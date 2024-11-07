
package com.sedeo.settlement.db.queries;

public class ParticipantQuery {

    private static final String PARTICIPANT_TABLE = "settlement_participant";

    public static final String STATUSES_PARAMETER = "statuses";
    public static final String USER_ID_PARAMETER = "userId";

    public static final String FIND_GROUP_IDS_FOR_PARTICIPANT_USER_ID_AND_SETTLEMENT_STATUS = "SELECT group_id FROM %s WHERE user_id = :%s AND settlement_status IN (:%s)"
            .formatted(PARTICIPANT_TABLE, USER_ID_PARAMETER, STATUSES_PARAMETER);
}
