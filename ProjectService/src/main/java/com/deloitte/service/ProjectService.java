package com.deloitte.service;

import java.util.List;
import java.util.Optional;

import com.deloitte.dto.IssueDto;
import com.deloitte.entity.Project;

public interface ProjectService {

    List<Project> getAllProjects();

    Optional<Project> getProjectById(Long projectId);

    Project saveProject(Project project);
    
    Project updateProject(Long projectId, Project project);

    //void deleteProject(Long projectId);
    
    List<IssueDto> getIssuesByProjectId(Long projectId);

    List<IssueDto> getIssuesByProjectName(String projectName);

    List<Project> getProjectsByOwnerId(Long ownerId);
}