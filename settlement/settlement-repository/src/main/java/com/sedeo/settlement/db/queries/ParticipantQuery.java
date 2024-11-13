
package com.sedeo.settlement.db.queries;

public class ParticipantQuery {

    private static final String PARTICIPANT_TABLE = "settlement_participant";

    public static final String STATUSES_PARAMETER = "statuses";
    public static final String USER_ID_PARAMETER = "userId";
    public static final String USER_IDS_PARAMETER = "userIds";
    public static final String USER_IDS_SIZE_PARAMETER = "userIdsSize";
    public static final String GROUP_ID_PARAMETER = "groupId";

    public static final String FIND_GROUP_IDS_FOR_PARTICIPANT_USER_ID_AND_SETTLEMENT_STATUS = "SELECT group_id FROM %s WHERE user_id = :%s AND settlement_status IN (:%s)"
            .formatted(PARTICIPANT_TABLE, USER_ID_PARAMETER, STATUSES_PARAMETER);

    public static final String SAVE_PARTICIPANT = "INSERT INTO %s VALUES(?, ?, ?, ?, ?)"
            .formatted(PARTICIPANT_TABLE);

    public static final String FIND_PARTICIPANTS_BY_GROUP_ID = "SELECT * FROM %s WHERE group_id = ?"
            .formatted(PARTICIPANT_TABLE);

    public static final String PARTICIPANT_EXISTS_BY_GROUP_ID_AND_USER_ID = "SELECT EXISTS (SELECT 1 FROM %s WHERE group_id = ? AND user_id = ?)"
            .formatted(PARTICIPANT_TABLE);

    public static final String PARTICIPANTS_EXIST_BY_GROUP_ID_AND_USER_IDS = ("SELECT COUNT(user_id) = :%s FROM %s WHERE group_id = :%s AND user_id IN (:%s)")
            .formatted(USER_IDS_SIZE_PARAMETER, PARTICIPANT_TABLE, GROUP_ID_PARAMETER, USER_IDS_PARAMETER);
}
