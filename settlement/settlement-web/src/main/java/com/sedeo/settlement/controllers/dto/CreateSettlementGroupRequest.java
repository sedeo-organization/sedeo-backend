package com.sedeo.settlement.controllers.dto;

import java.util.UUID;

public record CreateSettlementGroupRequest(UUID groupId, String title) {
}
