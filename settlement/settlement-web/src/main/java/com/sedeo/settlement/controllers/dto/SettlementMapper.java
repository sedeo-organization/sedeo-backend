package com.sedeo.settlement.controllers.dto;

import com.sedeo.settlement.model.Exchange;
import com.sedeo.settlement.model.Settlement;
import com.sedeo.settlement.model.SettlementGroup;
import com.sedeo.settlement.model.view.DetailedSettlement;
import com.sedeo.settlement.model.view.ExchangeWithParticipants;
import com.sedeo.settlement.model.view.SimpleSettlement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    Exchange createSettlementRequestExchangeToExchange(CreateSingleSettlementRequest.SettlementExchange settlementExchange);

    List<Exchange> createSettlementRequestExchangeListToExchangeList(List<CreateSingleSettlementRequest.SettlementExchange> settlementExchanges);

    @Mapping(source = "settlementExchanges", target = "exchanges")
    Settlement createSettlementRequestToSettlement(CreateSingleSettlementRequest createSingleSettlementRequest);

    FetchSettlementsResponse.Settlement simpleSettlementToFetchSettlementResponseSettlement(SimpleSettlement settlement);

    List<FetchSettlementsResponse.Settlement> simpleSettlementsToFetchSettlementsList(List<SimpleSettlement> settlements);

    default FetchSettlementsResponse simpleSettlementsToFetchSettlementsResponse(List<SimpleSettlement> settlements) {
        List<FetchSettlementsResponse.Settlement> fetchSettlementResponseSettlements = simpleSettlementsToFetchSettlementsList(settlements);
        return new FetchSettlementsResponse(fetchSettlementResponseSettlements);
    }

    @Mapping(source = "creditor.firstName", target = "creditorFirstName")
    @Mapping(source = "creditor.lastName", target = "creditorLastName")
    @Mapping(source = "debtor.firstName", target = "debtorFirstName")
    @Mapping(source = "debtor.lastName", target = "debtorLastName")
    FetchSettlementDetailsResponse.SettlementExchange exchangeWithParticipantsToFetchSettlementDetailsResponseSettlementExchange(ExchangeWithParticipants exchangeWithParticipants);

    List<FetchSettlementDetailsResponse.SettlementExchange> exchangeWithParticipantListToFetchSettlementDetailsResponseSettlementExchangeList(List<ExchangeWithParticipants> exchangesWithParticipants);

    @Mapping(source = "exchanges", target = "settlementExchanges")
    FetchSettlementDetailsResponse detailedSettlementToFetchSettlementDetailsResponse(DetailedSettlement detailedSettlement);
}
