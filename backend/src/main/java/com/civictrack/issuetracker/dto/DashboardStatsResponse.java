package com.civictrack.issuetracker.dto;

import java.util.Map;

public record DashboardStatsResponse(
        long totalIssues,
        Map<String, Long> byCategory,
        Map<String, Long> byStatus,
        Map<String, Long> byPriority,
        double avgResolutionHours,
        long resolvedCount,
        long openCount
) {
}
