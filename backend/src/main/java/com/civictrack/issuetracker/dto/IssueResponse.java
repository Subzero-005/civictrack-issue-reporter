package com.civictrack.issuetracker.dto;

import com.civictrack.issuetracker.entity.IssueCategory;
import com.civictrack.issuetracker.entity.IssuePriority;
import com.civictrack.issuetracker.entity.IssueStatus;

import java.time.Instant;
import java.util.List;

public record IssueResponse(
        Long id,
        String title,
        String description,
        IssueCategory category,
        IssueStatus status,
        IssuePriority priority,
        String photoUrl,
        Double latitude,
        Double longitude,
        String address,
        Long reporterId,
        String reporterName,
        long upvoteCount,
        boolean upvotedByMe,
        Instant createdAt,
        Instant updatedAt,
        Instant resolvedAt,
        List<RemarkResponse> remarks
) {
}
