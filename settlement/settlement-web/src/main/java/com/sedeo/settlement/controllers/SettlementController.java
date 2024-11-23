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

import java.security.Principal;
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
    public ResponseEntity<?> fetchSettlementGroups(@RequestParam(required = false) String status, Principal principal) {
        List<SettlementStatus> settlementStatuses = extractSettlementStatuses(status);

        UUID userId = UUID.fromString(principal.getName());
        return settlementGroups.fetchSettlementGroups(userId, settlementStatuses).fold(
                ResponseMapper::mapError,
                settlementGroups -> ResponseEntity.ok().body(SETTLEMENT_MAPPER.settlementGroupsToFetchSettlementGroupsResponse(settlementGroups))
        );
    }

    @PostMapping("/settlement-groups")
    public ResponseEntity<?> createSettlementGroup(@RequestBody @Valid CreateSettlementGroupRequest createSettlementGroupRequest,
                                                   Principal principal) {
         UUID userId = UUID.fromString(principal.getName());
        createSettlementGroupRequest.participantIds().add(userId);
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
    public ResponseEntity<?> createSingleSettlement(@RequestBody @Valid CreateSingleSettlementRequest createSingleSettlementRequest, @PathVariable("groupId") UUID groupId,
                                                    Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return settlements.createSettlement(SETTLEMENT_MAPPER.createSettlementRequestToSettlement(createSingleSettlementRequest), userId, groupId)
                .fold(
                        ResponseMapper::mapError,
                        success -> ResponseEntity.status(CREATED).build()
                );
    }

    @GetMapping("/settlement-groups/{groupId}/settlements")
    public ResponseEntity<?> fetchSettlements(@PathVariable("groupId") UUID groupId, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return settlements.fetchSettlements(groupId, userId).fold(
                ResponseMapper::mapError,
                settlements -> ResponseEntity.ok().body(SETTLEMENT_MAPPER.simpleSettlementsToFetchSettlementsResponse(settlements))
        );
    }

    @GetMapping("/settlement-groups/{groupId}/settlements/{settlementId}")
    public ResponseEntity<?> fetchSettlementDetails(@PathVariable("groupId") UUID groupId, @PathVariable("settlementId") UUID settlementId,
                                                    Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return settlements.fetchSettlementDetails(userId, groupId, settlementId).fold(
                ResponseMapper::mapError,
                settlementDetails -> ResponseEntity.ok().body(SETTLEMENT_MAPPER.detailedSettlementToFetchSettlementDetailsResponse(settlementDetails))
        );
    }

    @PatchMapping("/settlement-groups/{groupId}/settlements/{settlementId}/exchanges/{exchangeId}")
    public ResponseEntity<?> settleExchange(@PathVariable UUID groupId, @PathVariable UUID settlementId, @PathVariable UUID exchangeId,
                                            Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return settlements.settleExchange(userId, groupId, settlementId, exchangeId).fold(
                ResponseMapper::mapError,
                success -> ResponseEntity.ok().build()
        );
    }

    @GetMapping("/settlement-groups/{groupId}/participants")
    public ResponseEntity<?> fetchParticipants(@PathVariable("groupId") UUID groupId, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return settlements.fetchParticipants(userId, groupId).fold(
                ResponseMapper::mapError,
                participants -> ResponseEntity.ok().body(SETTLEMENT_MAPPER.participantsToFetchParticipantsResponse(participants))
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
