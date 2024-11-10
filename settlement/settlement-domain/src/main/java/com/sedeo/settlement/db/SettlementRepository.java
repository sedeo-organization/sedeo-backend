package com.sedeo.settlement.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.Settlement;
import com.sedeo.settlement.model.SimpleSettlement;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface SettlementRepository {

    Either<GeneralError, Settlement> save(Settlement settlement, UUID groupId);

    Either<GeneralError, List<SimpleSettlement>> find(UUID groupId);
}
