package com.deloitte.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deloitte.entity.Issue;
import com.deloitte.exception.IssueNotFoundException;
import com.deloitte.service.IssueService;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

	private static final Logger logger = LoggerFactory.getLogger(IssueController.class);

	@Autowired
	private IssueService issueService;

	@GetMapping
	public ResponseEntity<List<Issue>> getAllIssues() {

		try {

			logger.info("Fetching all issues");

			List<Issue> issues = issueService.getAllIssues();

			return ResponseEntity.status(HttpStatus.OK).body(issues);

		} catch (Exception e) {

			logger.error("Failed to fetch issues", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{issueId}")
	public ResponseEntity<Issue> getIssueById(@PathVariable Long issueId) {

		try {

			logger.info("Fetching issue with id: {}", issueId);

			Issue issue = issueService.getIssueById(issueId);

			return ResponseEntity.status(HttpStatus.OK).body(issue);

		} catch (IssueNotFoundException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to get issue with id: {}", issueId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping
	public ResponseEntity<Issue> createIssue(@RequestBody Issue issue) {

		try {

			logger.info("Creating issue: {}", issue.getSummary());

			Issue createdIssue = issueService.saveIssue(issue);

			return ResponseEntity.status(HttpStatus.CREATED).body(createdIssue);

		} catch (IllegalArgumentException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to create issue", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/{issueId}")
	public ResponseEntity<Issue> updateIssue(@PathVariable Long issueId, @RequestBody Issue issue) {

		try {

			logger.info("Updating issue with id: {}", issueId);

			Issue updatedIssue = issueService.updateIssue(issueId, issue);

			return ResponseEntity.status(HttpStatus.OK).body(updatedIssue);

		} catch (IssueNotFoundException e) {

			throw e;

		} catch (IllegalArgumentException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to update issue with id: {}", issueId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/project/{projectId}")
	public ResponseEntity<List<Issue>> getIssuesByProject(@PathVariable Long projectId) {

		try {

			logger.info("Fetching issues for project id: {}", projectId);

			List<Issue> issues = issueService.getIssuesByProjectId(projectId);

			return ResponseEntity.status(HttpStatus.OK).body(issues);

		} catch (IllegalArgumentException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to fetch issues for project id: {}", projectId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/assignee/{assigneeId}")
	public ResponseEntity<List<Issue>> getIssuesByAssignee(@PathVariable Long assigneeId) {

		try {

			logger.info("Fetching issues for assignee id: {}", assigneeId);

			List<Issue> issues = issueService.getIssuesByAssigneeId(assigneeId);

			return ResponseEntity.status(HttpStatus.OK).body(issues);

		} catch (IllegalArgumentException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to fetch issues for assignee id: {}", assigneeId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/owner/{ownerId}")
	public ResponseEntity<List<Issue>> getIssuesByOwner(@PathVariable("ownerId") Long ownerId) {

		try {

			logger.info("Fetching issues for owner id: {}", ownerId);

			List<Issue> issues = issueService.getIssuesByOwnerId(ownerId);

			return ResponseEntity.status(HttpStatus.OK).body(issues);

		} catch (IssueNotFoundException e) {

			throw e;

		} catch (IllegalArgumentException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to fetch issues for owner id: {}", ownerId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}