package com.sedeo.settlement.facade;

import com.sedeo.common.error.GeneralError;
import com.sedeo.settlement.db.SettlementRepository;
import com.sedeo.settlement.model.Settlement;
import com.sedeo.settlement.model.SimpleSettlement;
import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public class SettlementsFacade implements Settlements {

    private final SettlementRepository settlementRepository;

    public SettlementsFacade(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    @Override
    public Either<GeneralError, Void> createSettlement(Settlement settlement, UUID groupId) {
        return settlementRepository.save(settlement, groupId)
                .flatMap(result -> Either.right(null));
    }

    @Override
    public Either<GeneralError, List<SimpleSettlement>> fetchSettlements(UUID groupId) {
        return settlementRepository.find(groupId);
    }

}
