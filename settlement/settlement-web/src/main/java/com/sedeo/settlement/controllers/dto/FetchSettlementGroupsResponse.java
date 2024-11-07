package com.sedeo.settlement.controllers.dto;

import java.util.List;
import java.util.UUID;

public record FetchSettlementGroupsResponse(List<SettlementGroup> settlementGroups) {
    public record SettlementGroup(UUID groupId, String title) { }
}
