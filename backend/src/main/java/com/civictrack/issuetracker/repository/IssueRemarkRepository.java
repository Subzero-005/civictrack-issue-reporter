package com.civictrack.issuetracker.repository;

import com.civictrack.issuetracker.entity.IssueRemark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRemarkRepository extends JpaRepository<IssueRemark, Long> {
    List<IssueRemark> findByIssueIdOrderByCreatedAtAsc(Long issueId);
}
