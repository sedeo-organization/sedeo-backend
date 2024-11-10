package com.sedeo.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.model.Settlement;
import io.vavr.control.Either;

import java.util.UUID;

public interface Settlements {

    Either<GeneralError, Void> createSettlement(Settlement settlement, UUID groupId);
}
