package com.sedeo.domain.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.settlement.model.view.SummaryExchange;
import com.sedeo.domain.settlement.model.ExchangeStatus;
import com.sedeo.domain.settlement.model.SettlementGroup;
import com.sedeo.domain.settlement.model.SettlementStatus;
import io.vavr.control.Either;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SettlementGroups {

    Either<GeneralError, List<SettlementGroup>> fetchSettlementGroups(UUID userId, List<SettlementStatus> statuses);

    Either<GeneralError, Void> createSettlementGroup(UUID groupId, String title, Set<UUID> userIds);

    Either<GeneralError, List<SummaryExchange>> fetchSettlementGroupSummary(UUID groupId, UUID userId, List<ExchangeStatus> statuses);
}
