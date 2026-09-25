package com.civictrack.issuetracker.repository;

import com.civictrack.issuetracker.entity.IssueUpvote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueUpvoteRepository extends JpaRepository<IssueUpvote, IssueUpvote.Key> {
    long countByIssueId(Long issueId);
    boolean existsByIssueIdAndUserId(Long issueId, Long userId);
    void deleteByIssueIdAndUserId(Long issueId, Long userId);
}
