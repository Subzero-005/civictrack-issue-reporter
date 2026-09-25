package com.civictrack.issuetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "issue_upvotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(IssueUpvote.Key.class)
public class IssueUpvote {

    @Id
    @Column(name = "issue_id")
    private Long issueId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {
        private Long issueId;
        private Long userId;
    }
}
