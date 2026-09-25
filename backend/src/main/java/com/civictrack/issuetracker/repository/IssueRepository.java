package com.civictrack.issuetracker.repository;

import com.civictrack.issuetracker.entity.Issue;
import com.civictrack.issuetracker.entity.IssueCategory;
import com.civictrack.issuetracker.entity.IssuePriority;
import com.civictrack.issuetracker.entity.IssueStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue> {

    List<Issue> findByReporterId(Long reporterId);

    long countByCategory(IssueCategory category);
    long countByStatus(IssueStatus status);
    long countByPriority(IssuePriority priority);
}
