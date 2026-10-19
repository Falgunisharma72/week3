package com.deloitte.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.deloitte.client.ProjectFeign;
import com.deloitte.dto.ProjectDto;
import com.deloitte.entity.Issue;
import com.deloitte.exception.IssueNotFoundException;
import com.deloitte.repository.IssueRepository;

@Service
public class IssueServiceImpl implements IssueService {

	@Autowired
	private IssueRepository issueRepository;

	@Autowired
	private ProjectFeign projectFeign;

	@Autowired
	RestTemplate restTemplate;

	@Override
	public List<Issue> getAllIssues() {

		return issueRepository.findAll();
	}

	@Override
	public Issue getIssueById(Long issueId) {

		validateIssueId(issueId);

		return issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException("Issue not found with id: " + issueId));
	}

	@Override
	public Issue saveIssue(Issue issue) {

		validateIssue(issue);

		return issueRepository.save(issue);
	}

	/*
	 * @Override public Issue updateIssue(Long issueId, Issue issue) {
	 * 
	 * validateIssueId(issueId);
	 * 
	 * Issue existingIssue = issueRepository.findById(issueId) .orElseThrow(() ->
	 * new IssueNotFoundException("Issue not found with id: " + issueId));
	 * 
	 * validateIssue(issue);
	 * 
	 * issue.setIssueId(existingIssue.getIssueId());
	 * 
	 * return issueRepository.save(issue); }
	 */

	@Override
	public Issue updateIssue(Long issueId, Issue issue) {
		validateIssueId(issueId);

		if (issue == null) {
			throw new IllegalArgumentException("Update data cannot be null");
		}

		Issue existingIssue = issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException("Issue not found with id: " + issueId));

		boolean updated = false;

		Long assigneeId = issue.getAssigneeId();

		if (assigneeId != null && assigneeId < 0) {
			throw new IllegalArgumentException("Assignee ID cannot be negative");
		}

		if (assigneeId != null && assigneeId > 0) {
			validateAssigneeId(assigneeId);
			existingIssue.setAssigneeId(assigneeId);
			updated = true;
		}

		if (issue.getDescription() != null) {
			existingIssue.setDescription(issue.getDescription());
			updated = true;
		}

		if (issue.getPriority() != null) {
			existingIssue.setPriority(issue.getPriority());
			updated = true;
		}

		Long projectId = issue.getProjectId();

		if (projectId != null && projectId < 0) {
			throw new IllegalArgumentException("Project ID cannot be negative");
		}

		if (projectId != null && projectId > 0) {
			validateProjectId(projectId);
			existingIssue.setProjectId(projectId);
			updated = true;
		}

		if (issue.getSprint() != null) {
			existingIssue.setSprint(issue.getSprint());
			updated = true;
		}

		if (issue.getStatus() != null) {
			existingIssue.setStatus(issue.getStatus());
			updated = true;
		}

		if (issue.getStoryPoint() != null) {
			validateStoryPoint(issue.getStoryPoint());
			existingIssue.setStoryPoint(issue.getStoryPoint());
			updated = true;
		}

		if (issue.getSummary() != null) {
			existingIssue.setSummary(issue.getSummary());
			updated = true;
		}

		if (issue.getTag() != null) {
			existingIssue.setTag(issue.getTag());
			updated = true;
		}

		if (issue.getType() != null) {
			existingIssue.setType(issue.getType());
			updated = true;
		}

		if (!updated) {
			throw new IllegalArgumentException("At least one valid field must be provided for update");
		}

		return issueRepository.save(existingIssue);
	}

	@Override
	public List<Issue> getIssuesByProjectId(Long projectId) {

		validateProjectId(projectId);

		List<Issue> issues = issueRepository.findByProjectId(projectId);

		if (issues.isEmpty()) {

			throw new IssueNotFoundException("No issues found for project id: " + projectId);
		}

		return issues;
	}

	@Override
	public List<Issue> getIssuesByAssigneeId(Long assigneeId) {

		validateAssigneeId(assigneeId);

		List<Issue> issues = issueRepository.findByAssigneeId(assigneeId);

		if (issues.isEmpty()) {

			throw new IssueNotFoundException("No issues found for assignee id: " + assigneeId);
		}

		return issues;
	}

	/*
	 * @Override public List<Issue> getIssuesByOwnerId(Long ownerId) {
	 * 
	 * validateOwnerId(ownerId);
	 * 
	 * // Get projects owned by this user ResponseEntity<List<ProjectDto>> response
	 * = projectFeign.getProjectsByOwnerId(ownerId);
	 * 
	 * List<ProjectDto> projects = response.getBody();
	 * 
	 * List<Issue> allIssues = new ArrayList<>();
	 * 
	 * if (projects != null) {
	 * 
	 * for (ProjectDto project : projects) {
	 * 
	 * List<Issue> issues = issueRepository.findByProjectId(project.getProjectId());
	 * 
	 * allIssues.addAll(issues); } }
	 * 
	 * return allIssues; }
	 */

	@Override
	public List<Issue> getIssuesByOwnerId(Long ownerId) {
		validateOwnerId(ownerId);

		String url = "http://localhost:8082/api/projects/owner/" + ownerId;

		ProjectDto[] projectArray = restTemplate.getForObject(url, ProjectDto[].class);

		List<Issue> allIssues = new ArrayList<>();

		if (projectArray != null) {
			for (ProjectDto project : projectArray) {
				List<Issue> issues = issueRepository.findByProjectId(project.getProjectId());

				allIssues.addAll(issues);
			}
		}

		return allIssues;
	}

	private void validateIssueId(Long issueId) {

		if (issueId == null || issueId <= 0) {

			throw new IllegalArgumentException("Issue ID must be greater than 0");
		}
	}

	private void validateIssue(Issue issue) {

		validateProjectId(issue.getProjectId());

		validateAssigneeId(issue.getAssigneeId());

		validateStoryPoint(issue.getStoryPoint());
	}

	private void validateProjectId(Long projectId) {

		if (projectId == null || projectId <= 0) {

			throw new IllegalArgumentException("Project ID must be greater than 0");
		}
	}

	private void validateAssigneeId(Long assigneeId) {

		if (assigneeId == null || assigneeId <= 0) {

			throw new IllegalArgumentException("Assignee ID must be greater than 0");
		}
	}

	private void validateStoryPoint(Integer storyPoint) {

		if (storyPoint == null || storyPoint <= 0) {

			throw new IllegalArgumentException("Story point must be greater than 0");
		}
	}

	private void validateOwnerId(Long ownerId) {

		if (ownerId == null || ownerId <= 0) {

			throw new IllegalArgumentException("Owner ID must be greater than 0");
		}
	}
}