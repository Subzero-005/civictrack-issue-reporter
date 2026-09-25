package com.civictrack.issuetracker.controller;

import com.civictrack.issuetracker.dto.IssueCreateRequest;
import com.civictrack.issuetracker.dto.IssueResponse;
import com.civictrack.issuetracker.dto.IssueStatusUpdateRequest;
import com.civictrack.issuetracker.dto.RemarkRequest;
import com.civictrack.issuetracker.entity.IssueCategory;
import com.civictrack.issuetracker.entity.IssuePriority;
import com.civictrack.issuetracker.entity.IssueStatus;
import com.civictrack.issuetracker.security.CurrentUser;
import com.civictrack.issuetracker.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final CurrentUser currentUser;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<IssueResponse> create(@Valid @ModelAttribute IssueCreateRequest request,
                                                 @RequestPart(required = false) MultipartFile photo) {
        IssueResponse created = issueService.createIssue(currentUser.id(), request, photo);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public Page<IssueResponse> list(@RequestParam(required = false) IssueCategory category,
                                     @RequestParam(required = false) IssueStatus status,
                                     @RequestParam(required = false) IssuePriority priority,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(required = false, name = "mine") Boolean mineOnly,
                                     @PageableDefault(size = 20) Pageable pageable) {
        Long reporterFilter = Boolean.TRUE.equals(mineOnly) ? currentUser.id() : null;
        return issueService.listIssues(category, status, priority, keyword, reporterFilter, currentUser.id(), pageable);
    }

    @GetMapping("/{id}")
    public IssueResponse get(@PathVariable Long id) {
        return issueService.getIssue(id, currentUser.id());
    }

    @PostMapping("/{id}/upvote")
    public IssueResponse toggleUpvote(@PathVariable Long id) {
        return issueService.toggleUpvote(id, currentUser.id());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public IssueResponse updateStatus(@PathVariable Long id, @RequestBody IssueStatusUpdateRequest request) {
        return issueService.updateStatus(id, request);
    }

    @PostMapping("/{id}/remarks")
    @PreAuthorize("hasRole('ADMIN')")
    public IssueResponse addRemark(@PathVariable Long id, @Valid @RequestBody RemarkRequest request) {
        return issueService.addRemark(id, currentUser.id(), request);
    }
}
