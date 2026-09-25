package com.civictrack.issuetracker.dto;

import java.time.Instant;

public record RemarkResponse(
        Long id,
        String adminName,
        String note,
        Instant createdAt
) {
}
