package com.sedeo.settlement.db;

import com.sedeo.settlement.db.model.SettlementGroupEntity;
import com.sedeo.settlement.model.SettlementGroup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SettlementMapper {

    SettlementMapper INSTANCE = Mappers.getMapper(SettlementMapper.class);

    SettlementGroup settlementGroupEntityToSettlementGroup(SettlementGroupEntity settlementGroupEntity);

    List<SettlementGroup> settlementGroupEntityListToSettlementGroup(List<SettlementGroupEntity> settlementGroupEntityList);
}
