package com.sedeo.settlement.controllers.dto;

import java.util.UUID;

public record BulkSettleGroupExchangesRequest(UUID debtorUserId, UUID creditorUserId) {
}
