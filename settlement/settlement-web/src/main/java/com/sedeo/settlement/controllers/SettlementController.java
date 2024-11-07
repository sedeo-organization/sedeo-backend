package com.sedeo.settlement.controllers;

import com.sedeo.settlement.controllers.dto.SettlementMapper;
import com.sedeo.settlement.facade.SettlementGroups;
import com.sedeo.settlement.model.SettlementStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
public class SettlementController {

    private static final SettlementMapper SETTLEMENT_MAPPER = SettlementMapper.INSTANCE;

    private final SettlementGroups settlementGroups;

    public SettlementController(SettlementGroups settlementGroups) {
        this.settlementGroups = settlementGroups;
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
