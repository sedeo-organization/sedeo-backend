package com.sedeo.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.Settlement;
import com.sedeo.settlement.model.SimpleSettlement;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface Settlements {

    Either<GeneralError, Void> createSettlement(Settlement settlement, UUID groupId);

    Either<GeneralError, List<SimpleSettlement>> fetchSettlements(UUID groupId);
}
