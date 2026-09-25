package com.civictrack.issuetracker.dto;

import jakarta.validation.constraints.NotBlank;

public record RemarkRequest(
        @NotBlank String note
) {
}
