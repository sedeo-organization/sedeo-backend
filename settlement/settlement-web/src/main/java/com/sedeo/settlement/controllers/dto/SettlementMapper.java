package com.sedeo.settlement.controllers.dto;

import com.sedeo.domain.settlement.model.Exchange;
import com.sedeo.domain.settlement.model.Participant;
import com.sedeo.domain.settlement.model.Settlement;
import com.sedeo.domain.settlement.model.SettlementGroup;
import com.sedeo.domain.settlement.model.view.DetailedSettlement;
import com.sedeo.domain.settlement.model.view.ExchangeWithParticipants;
import com.sedeo.domain.settlement.model.view.SimpleSettlement;
import com.sedeo.domain.settlement.model.view.SummaryExchange;
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
    @Mapping(source = "creditor.userId", target = "creditorId")
    @Mapping(source = "creditor.lastName", target = "creditorLastName")
    @Mapping(source = "debtor.firstName", target = "debtorFirstName")
    @Mapping(source = "debtor.lastName", target = "debtorLastName")
    @Mapping(source = "debtor.userId", target = "debtorId")
    FetchSettlementDetailsResponse.SettlementExchange exchangeWithParticipantsToFetchSettlementDetailsResponseSettlementExchange(ExchangeWithParticipants exchangeWithParticipants);

    List<FetchSettlementDetailsResponse.SettlementExchange> exchangeWithParticipantListToFetchSettlementDetailsResponseSettlementExchangeList(List<ExchangeWithParticipants> exchangesWithParticipants);

    @Mapping(source = "exchanges", target = "settlementExchanges")
    FetchSettlementDetailsResponse detailedSettlementToFetchSettlementDetailsResponse(DetailedSettlement detailedSettlement);

    FetchParticipantsResponse.Participant participantToFetchParticipantsResponseParticipant(Participant participant);

    List<FetchParticipantsResponse.Participant> participantListToFetchParticipantsResponseParticipantList(List<Participant> participants);

    default FetchParticipantsResponse participantsToFetchParticipantsResponse(List<Participant> participants) {
        List<FetchParticipantsResponse.Participant> fetchParticipantsResponseParticipantList = participantListToFetchParticipantsResponseParticipantList(participants);
        return new FetchParticipantsResponse(fetchParticipantsResponseParticipantList);
    }

    FetchSettlementGroupSummaryResponse.SummaryExchange summaryExchangeToFetchSettlementGroupSummaryResponseSummaryExchange(SummaryExchange summaryExchange);

    List<FetchSettlementGroupSummaryResponse.SummaryExchange> summaryExchangeListToFetchSettlementGroupSummaryResponseSummaryExchangeList(List<SummaryExchange> summaryExchanges);

    default FetchSettlementGroupSummaryResponse summaryExchangesToFetchSettlementGroupSummaryResponse(List<SummaryExchange> summaryExchanges) {
        List<FetchSettlementGroupSummaryResponse.SummaryExchange> fetchSettlementGroupSummaryResponseSummaryExchanges = summaryExchangeListToFetchSettlementGroupSummaryResponseSummaryExchangeList(summaryExchanges);
        return new FetchSettlementGroupSummaryResponse(fetchSettlementGroupSummaryResponseSummaryExchanges);
    }
}
