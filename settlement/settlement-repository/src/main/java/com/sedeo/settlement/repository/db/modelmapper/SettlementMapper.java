package com.sedeo.settlement.repository.db.modelmapper;

import com.sedeo.settlement.repository.db.model.SettlementEntity;
import com.sedeo.domain.settlement.model.Exchange;
import com.sedeo.domain.settlement.model.Settlement;
import com.sedeo.domain.settlement.model.view.SimpleSettlement;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SettlementMapper {

    SettlementMapper INSTANCE = Mappers.getMapper(SettlementMapper.class);

    Settlement settlementEntityToSettlement(SettlementEntity settlementEntity, List<Exchange> exchanges);

    SimpleSettlement settlementEntityToSimpleSettlement(SettlementEntity settlementEntity);

    List<SimpleSettlement> settlementEntityListToSimpleSettlementList(List<SettlementEntity> settlementEntities);
}
