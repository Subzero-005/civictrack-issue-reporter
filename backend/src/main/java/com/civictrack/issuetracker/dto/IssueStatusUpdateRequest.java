package com.civictrack.issuetracker.dto;

import com.civictrack.issuetracker.entity.IssuePriority;
import com.civictrack.issuetracker.entity.IssueStatus;

public record IssueStatusUpdateRequest(
        IssueStatus status,
        IssuePriority priority
) {
}
