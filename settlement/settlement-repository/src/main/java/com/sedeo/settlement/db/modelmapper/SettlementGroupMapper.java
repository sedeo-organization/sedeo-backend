package com.sedeo.settlement.db.modelmapper;

import com.sedeo.settlement.db.model.SettlementGroupEntity;
import com.sedeo.settlement.model.SettlementGroup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SettlementGroupMapper {

    SettlementGroupMapper INSTANCE = Mappers.getMapper(SettlementGroupMapper.class);

    SettlementGroup settlementGroupEntityToSettlementGroup(SettlementGroupEntity settlementGroupEntity);

    List<SettlementGroup> settlementGroupEntityListToSettlementGroup(List<SettlementGroupEntity> settlementGroupEntityList);
}
