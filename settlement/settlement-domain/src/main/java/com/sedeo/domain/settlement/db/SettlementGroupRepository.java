package com.sedeo.domain.settlement.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.settlement.model.SettlementGroup;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface SettlementGroupRepository {

    Either<GeneralError, List<SettlementGroup>> findSettlementGroups(List<UUID> groupIds);

    Either<GeneralError, SettlementGroup> save(SettlementGroup settlementGroup);

    Boolean exists(UUID groupId);
}
