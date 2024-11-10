package com.sedeo.settlement.controllers;

import com.sedeo.settlement.controllers.dto.CreateSettlementGroupRequest;
import com.sedeo.settlement.controllers.dto.CreateSingleSettlementRequest;
import com.sedeo.settlement.controllers.dto.SettlementMapper;
import com.sedeo.settlement.facade.SettlementGroups;
import com.sedeo.settlement.facade.Settlements;
import com.sedeo.settlement.model.Settlement;
import com.sedeo.settlement.model.SettlementStatus;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> createSettlementGroup(@RequestBody CreateSettlementGroupRequest createSettlementGroupRequest) {
        //TODO: Change UUID so that it is extracted from the token
        return settlementGroups.createSettlementGroup(createSettlementGroupRequest.groupId(),
                UUID.fromString("c9d1b5f0-8a6a-4e1d-84c9-bfede64e659d"),
                createSettlementGroupRequest.title()
        ).fold(
                ResponseMapper::mapError,
                settlementGroups -> ResponseEntity.status(CREATED).build()
        );
    }

    @PostMapping("/settlement-groups/{groupId}/settlements")
    public ResponseEntity<?> createSingleSettlement(@RequestBody CreateSingleSettlementRequest createSingleSettlementRequest, @PathVariable("groupId") UUID groupId) {
        return settlements.createSettlement(SETTLEMENT_MAPPER.createSettlementRequestToSettlement(createSingleSettlementRequest), groupId)
                .fold(
                        ResponseMapper::mapError,
                        success -> ResponseEntity.status(CREATED).build()
                );
    }

    @GetMapping("/settlement-groups/{groupId}/settlements")
    public ResponseEntity<?> fetchSettlements(@PathVariable("groupId") UUID groupId) {
        return settlements.fetchSettlements(groupId).fold(
                ResponseMapper::mapError,
                settlements -> ResponseEntity.ok().body(SETTLEMENT_MAPPER.simpleSettlementsToFetchSettlementsResponse(settlements))
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
