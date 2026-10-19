package com.deloitte.service;

import java.util.List;

import com.deloitte.entity.Issue;

public interface IssueService {

    List<Issue> getAllIssues();

    Issue getIssueById(Long issueId);

    Issue saveIssue(Issue issue);

    Issue updateIssue(Long issueId, Issue issue);

    List<Issue> getIssuesByProjectId(Long projectId);

    List<Issue> getIssuesByAssigneeId(Long assigneeId);
    
    List<Issue> getIssuesByOwnerId(Long ownerId);
}