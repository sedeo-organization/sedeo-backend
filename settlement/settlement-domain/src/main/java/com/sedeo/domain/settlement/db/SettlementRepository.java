package com.sedeo.domain.settlement.db;

import com.sedeo.common.error.GeneralError;
import com.sedeo.domain.settlement.model.Settlement;
import com.sedeo.domain.settlement.model.view.SimpleSettlement;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface SettlementRepository {

    Either<GeneralError, Settlement> save(Settlement settlement, UUID groupId);

    Either<GeneralError, List<SimpleSettlement>> find(UUID groupId);

    Either<GeneralError, Settlement> findSettlement(UUID settlementId);

    Boolean exists(UUID settlementId);

    Either<GeneralError, Settlement> update(Settlement settlement, UUID groupId);
}
