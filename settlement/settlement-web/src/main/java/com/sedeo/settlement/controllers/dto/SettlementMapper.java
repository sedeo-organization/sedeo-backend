package com.sedeo.settlement.controllers.dto;

import com.sedeo.settlement.model.SettlementGroup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SettlementMapper {

    SettlementMapper INSTANCE = Mappers.getMapper(SettlementMapper.class);

    FetchSettlementGroupsResponse.SettlementGroup settlementGroupToFetchSettlementGroupResponseSettlementGroup(SettlementGroup settlementGroup);

    List<FetchSettlementGroupsResponse.SettlementGroup> settlementGroupsToFetchSettlementGroupResponseSettlementGroups(List<SettlementGroup> settlementGroups);

    default FetchSettlementGroupsResponse settlementGroupsToFetchSettlementGroupsResponse(List<SettlementGroup> settlementGroups) {
        List<FetchSettlementGroupsResponse.SettlementGroup> settlementGroupList = settlementGroupsToFetchSettlementGroupResponseSettlementGroups(settlementGroups);
        return new FetchSettlementGroupsResponse(settlementGroupList);
    }
}
