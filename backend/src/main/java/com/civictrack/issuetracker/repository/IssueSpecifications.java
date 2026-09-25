package com.civictrack.issuetracker.repository;

import com.civictrack.issuetracker.entity.Issue;
import com.civictrack.issuetracker.entity.IssueCategory;
import com.civictrack.issuetracker.entity.IssuePriority;
import com.civictrack.issuetracker.entity.IssueStatus;
import org.springframework.data.jpa.domain.Specification;

public final class IssueSpecifications {

    private IssueSpecifications() {
    }

    public static Specification<Issue> category(IssueCategory category) {
        return (root, query, cb) -> category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<Issue> status(IssueStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Issue> priority(IssuePriority priority) {
        return (root, query, cb) -> priority == null ? null : cb.equal(root.get("priority"), priority);
    }

    public static Specification<Issue> reporterId(Long reporterId) {
        return (root, query, cb) -> reporterId == null ? null : cb.equal(root.get("reporter").get("id"), reporterId);
    }

    public static Specification<Issue> keyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return (root, query, cb) -> null;
        }
        String like = "%" + keyword.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), like),
                cb.like(cb.lower(root.get("description")), like),
                cb.like(cb.lower(root.get("address")), like));
    }
}
