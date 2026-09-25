package com.civictrack.issuetracker.service;

import com.civictrack.issuetracker.dto.DashboardStatsResponse;
import com.civictrack.issuetracker.entity.Issue;
import com.civictrack.issuetracker.entity.IssueCategory;
import com.civictrack.issuetracker.entity.IssuePriority;
import com.civictrack.issuetracker.entity.IssueStatus;
import com.civictrack.issuetracker.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IssueRepository issueRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats() {
        List<Issue> allIssues = issueRepository.findAll();

        Map<String, Long> byCategory = new LinkedHashMap<>();
        for (IssueCategory c : IssueCategory.values()) {
            byCategory.put(c.name(), issueRepository.countByCategory(c));
        }

        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (IssueStatus s : IssueStatus.values()) {
            byStatus.put(s.name(), issueRepository.countByStatus(s));
        }

        Map<String, Long> byPriority = new LinkedHashMap<>();
        for (IssuePriority p : IssuePriority.values()) {
            byPriority.put(p.name(), issueRepository.countByPriority(p));
        }

        long resolvedCount = byStatus.getOrDefault("RESOLVED", 0L) + byStatus.getOrDefault("CLOSED", 0L);
        long openCount = allIssues.size() - resolvedCount;

        double avgResolutionHours = allIssues.stream()
                .filter(i -> i.getResolvedAt() != null)
                .mapToLong(i -> Duration.between(i.getCreatedAt(), i.getResolvedAt()).toMinutes())
                .average()
                .orElse(0.0) / 60.0;

        return new DashboardStatsResponse(
                allIssues.size(),
                byCategory,
                byStatus,
                byPriority,
                Math.round(avgResolutionHours * 10) / 10.0,
                resolvedCount,
                openCount
        );
    }
}
