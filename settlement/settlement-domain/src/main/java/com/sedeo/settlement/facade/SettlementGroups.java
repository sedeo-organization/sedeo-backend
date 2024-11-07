package com.sedeo.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.SettlementGroup;
import com.sedeo.settlement.model.SettlementStatus;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface SettlementGroups {

    Either<GeneralError, List<SettlementGroup>> fetchSettlementGroups(UUID userId, List<SettlementStatus> statuses);
}
