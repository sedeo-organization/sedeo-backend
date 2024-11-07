package com.sedeo.settlement.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.SettlementGroup;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface SettlementGroupRepository {

    Either<GeneralError, List<SettlementGroup>> findUsersSettlementGroups(List<UUID> groupIds);
}
