package com.civictrack.issuetracker.service;

import com.civictrack.issuetracker.dto.*;
import com.civictrack.issuetracker.entity.*;
import com.civictrack.issuetracker.exception.ApiException;
import com.civictrack.issuetracker.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final IssueRemarkRepository remarkRepository;
    private final IssueUpvoteRepository upvoteRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public IssueResponse createIssue(Long reporterId, IssueCreateRequest request, MultipartFile photo) {
        User reporter = getUser(reporterId);
        String photoUrl = fileStorageService.store(photo);

        Issue issue = Issue.builder()
                .reporter(reporter)
                .title(request.title())
                .description(request.description())
                .category(request.category())
                .status(IssueStatus.REPORTED)
                .priority(IssuePriority.MEDIUM)
                .photoUrl(photoUrl)
                .latitude(request.latitude())
                .longitude(request.longitude())
                .address(request.address())
                .build();

        issue = issueRepository.save(issue);
        return toResponse(issue, reporterId);
    }

    @Transactional(readOnly = true)
    public Page<IssueResponse> listIssues(IssueCategory category, IssueStatus status, IssuePriority priority,
                                           String keyword, Long onlyReporterId, Long currentUserId, Pageable pageable) {
        Specification<Issue> spec = Specification
                .where(IssueSpecifications.category(category))
                .and(IssueSpecifications.status(status))
                .and(IssueSpecifications.priority(priority))
                .and(IssueSpecifications.reporterId(onlyReporterId))
                .and(IssueSpecifications.keyword(keyword));

        return issueRepository.findAll(spec, pageable).map(issue -> toResponse(issue, currentUserId));
    }

    @Transactional(readOnly = true)
    public IssueResponse getIssue(Long issueId, Long currentUserId) {
        Issue issue = getIssueOrThrow(issueId);
        return toResponse(issue, currentUserId);
    }

    @Transactional
    public IssueResponse updateStatus(Long issueId, IssueStatusUpdateRequest request) {
        Issue issue = getIssueOrThrow(issueId);

        if (request.status() != null) {
            issue.setStatus(request.status());
            if (request.status() == IssueStatus.RESOLVED || request.status() == IssueStatus.CLOSED) {
                issue.setResolvedAt(java.time.Instant.now());
            } else {
                issue.setResolvedAt(null);
            }
        }
        if (request.priority() != null) {
            issue.setPriority(request.priority());
        }

        issue = issueRepository.save(issue);
        return toResponse(issue, null);
    }

    @Transactional
    public IssueResponse addRemark(Long issueId, Long adminId, RemarkRequest request) {
        Issue issue = getIssueOrThrow(issueId);
        User admin = getUser(adminId);

        IssueRemark remark = IssueRemark.builder()
                .issue(issue)
                .admin(admin)
                .note(request.note())
                .build();
        remarkRepository.save(remark);

        return toResponse(issue, null);
    }

    @Transactional
    public IssueResponse toggleUpvote(Long issueId, Long userId) {
        Issue issue = getIssueOrThrow(issueId);

        if (upvoteRepository.existsByIssueIdAndUserId(issueId, userId)) {
            upvoteRepository.deleteByIssueIdAndUserId(issueId, userId);
        } else {
            upvoteRepository.save(IssueUpvote.builder().issueId(issueId).userId(userId).build());
        }

        return toResponse(issue, userId);
    }

    private Issue getIssueOrThrow(Long issueId) {
        return issueRepository.findById(issueId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Issue not found"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private IssueResponse toResponse(Issue issue, Long currentUserId) {
        List<RemarkResponse> remarks = remarkRepository.findByIssueIdOrderByCreatedAtAsc(issue.getId()).stream()
                .map(r -> new RemarkResponse(r.getId(), r.getAdmin().getName(), r.getNote(), r.getCreatedAt()))
                .toList();

        long upvoteCount = upvoteRepository.countByIssueId(issue.getId());
        boolean upvotedByMe = currentUserId != null && upvoteRepository.existsByIssueIdAndUserId(issue.getId(), currentUserId);

        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getCategory(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getPhotoUrl(),
                issue.getLatitude(),
                issue.getLongitude(),
                issue.getAddress(),
                issue.getReporter().getId(),
                issue.getReporter().getName(),
                upvoteCount,
                upvotedByMe,
                issue.getCreatedAt(),
                issue.getUpdatedAt(),
                issue.getResolvedAt(),
                remarks
        );
    }
}
