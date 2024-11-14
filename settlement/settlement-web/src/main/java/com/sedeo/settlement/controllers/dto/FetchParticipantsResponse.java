package com.sedeo.settlement.controllers.dto;

import java.util.List;
import java.util.UUID;

public record FetchParticipantsResponse(List<Participant> participants) {
    public record Participant(UUID userId, String firstName, String lastName) { }
}
