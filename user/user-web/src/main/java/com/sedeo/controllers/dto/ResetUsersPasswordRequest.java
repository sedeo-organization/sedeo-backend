package com.sedeo.controllers.dto;

import java.util.UUID;

public record ResetUsersPasswordRequest(UUID token, String password) {
}
