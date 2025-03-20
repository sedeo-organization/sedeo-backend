package com.sedeo.settlement.repository.db.modelmapper;

import com.sedeo.settlement.repository.db.model.SettlementGroupEntity;
import com.sedeo.domain.settlement.model.SettlementGroup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SettlementGroupModelMapper {

    SettlementGroupModelMapper INSTANCE = Mappers.getMapper(SettlementGroupModelMapper.class);

    SettlementGroup settlementGroupEntityToSettlementGroup(SettlementGroupEntity settlementGroupEntity);

    List<SettlementGroup> settlementGroupEntityListToSettlementGroup(List<SettlementGroupEntity> settlementGroupEntityList);
}
