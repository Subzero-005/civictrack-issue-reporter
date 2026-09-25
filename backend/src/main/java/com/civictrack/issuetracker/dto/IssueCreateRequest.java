package com.civictrack.issuetracker.dto;

import com.civictrack.issuetracker.entity.IssueCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IssueCreateRequest(
        @NotBlank @Size(max = 160) String title,
        @NotBlank String description,
        @NotNull IssueCategory category,
        @NotNull Double latitude,
        @NotNull Double longitude,
        @Size(max = 255) String address
) {
}
