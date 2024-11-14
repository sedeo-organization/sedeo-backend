package com.sedeo.settlement.controllers;

import com.sedeo.settlement.controllers.dto.CreateSettlementGroupRequest;
import com.sedeo.settlement.controllers.dto.CreateSingleSettlementRequest;
import com.sedeo.settlement.controllers.dto.SettlementMapper;
import com.sedeo.settlement.facade.SettlementGroups;
import com.sedeo.settlement.facade.Settlements;
import com.sedeo.settlement.model.SettlementStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class SettlementController {

    private static final SettlementMapper SETTLEMENT_MAPPER = SettlementMapper.INSTANCE;

    private final SettlementGroups settlementGroups;
    private final Settlements settlements;

    public SettlementController(SettlementGroups settlementGroups, Settlements settlements) {
        this.settlementGroups = settlementGroups;
        this.settlements = settlements;
    }

    @GetMapping("/settlement-groups")
    public ResponseEntity<?> fetchSettlementGroups(@RequestParam(required = false) String status) {
        List<SettlementStatus> settlementStatuses = extractSettlementStatuses(status);

        //TODO: Change UUID so that it is extracted from the token
        return settlementGroups.fetchSettlementGroups(UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e659d"), settlementStatuses).fold(
                ResponseMapper::mapError,
                settlementGroups -> ResponseEntity.ok().body(SETTLEMENT_MAPPER.settlementGroupsToFetchSettlementGroupsResponse(settlementGroups))
        );
    }

    @PostMapping("/settlement-groups")
    public ResponseEntity<?> createSettlementGroup(@RequestBody @Valid CreateSettlementGroupRequest createSettlementGroupRequest) {
        //TODO: Change UUID so that it is extracted from the token
        createSettlementGroupRequest.participantIds().add(UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e659d"));
        return settlementGroups.createSettlementGroup(
                createSettlementGroupRequest.groupId(),
                createSettlementGroupRequest.title(),
                createSettlementGroupRequest.participantIds()
        ).fold(
                ResponseMapper::mapError,
                settlementGroups -> ResponseEntity.status(CREATED).build()
        );
    }

    @PostMapping("/settlement-groups/{groupId}/settlements")
    public ResponseEntity<?> createSingleSettlement(@RequestBody @Valid CreateSingleSettlementRequest createSingleSettlementRequest, @PathVariable("groupId") UUID groupId) {
        //TODO: Change UUID so that it is extracted from the token
        UUID principal = UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e659d");
        return settlements.createSettlement(SETTLEMENT_MAPPER.createSettlementRequestToSettlement(createSingleSettlementRequest), principal, groupId)
                .fold(
                        ResponseMapper::mapError,
                        success -> ResponseEntity.status(CREATED).build()
                );
    }

    @GetMapping("/settlement-groups/{groupId}/settlements")
    public ResponseEntity<?> fetchSettlements(@PathVariable("groupId") UUID groupId) {
        //TODO: Change UUID so that it is extracted from the token
        UUID principal = UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e659d");
        return settlements.fetchSettlements(groupId, principal).fold(
                ResponseMapper::mapError,
                settlements -> ResponseEntity.ok().body(SETTLEMENT_MAPPER.simpleSettlementsToFetchSettlementsResponse(settlements))
        );
    }

    @GetMapping("/settlement-groups/{groupId}/settlements/{settlementId}")
    public ResponseEntity<?> fetchSettlementDetails(@PathVariable("groupId") UUID groupId, @PathVariable("settlementId") UUID settlementId) {
        //TODO: Change UUID so that it is extracted from the token
        UUID principal = UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e659d");
        return settlements.fetchSettlementDetails(principal, groupId, settlementId).fold(
                ResponseMapper::mapError,
                settlementDetails -> ResponseEntity.ok().body(SETTLEMENT_MAPPER.detailedSettlementToFetchSettlementDetailsResponse(settlementDetails))
        );
    }

    @PatchMapping("/settlement-groups/{groupId}/settlements/{settlementId}/exchanges/{exchangeId}")
    public ResponseEntity<?> settleExchange(@PathVariable UUID groupId, @PathVariable UUID settlementId, @PathVariable UUID exchangeId) {
        //TODO: Change UUID so that it is extracted from the token
        UUID principal = UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e658d");
        return settlements.settleExchange(principal, groupId, settlementId, exchangeId).fold(
                ResponseMapper::mapError,
                success -> ResponseEntity.ok().build()
        );
    }

    private static List<SettlementStatus> extractSettlementStatuses(String status) {
        if (Objects.equals(status, SettlementStatus.PENDING.name().toLowerCase())) {
            return List.of(SettlementStatus.PENDING);
        } else if (Objects.equals(status, SettlementStatus.SETTLED.name().toLowerCase())) {
            return List.of(SettlementStatus.SETTLED);
        } else {
            return List.of(SettlementStatus.SETTLED, SettlementStatus.PENDING);
        }
    }
}
