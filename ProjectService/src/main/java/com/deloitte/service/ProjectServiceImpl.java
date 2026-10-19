package com.deloitte.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.deloitte.client.IssueFeign;
import com.deloitte.dto.IssueDto;
import com.deloitte.entity.Project;
import com.deloitte.exception.ProjectNotFoundException;
import com.deloitte.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private IssueFeign issueFeign;

	@Override
	public List<Project> getAllProjects() {
		return projectRepository.findAll();
	}

	@Override
	public Optional<Project> getProjectById(Long projectId) {

		validateProjectId(projectId);

		Optional<Project> project = projectRepository.findById(projectId);

		if (project.isEmpty()) {
			throw new ProjectNotFoundException("Project not found with id: " + projectId);
		}

		return project;
	}

	@Override
	public Project saveProject(Project project) {

		validateProject(project);

		return projectRepository.save(project);
	}

	/*
	 * @Override public Project updateProject(Long projectId, Project project) {
	 * 
	 * validateProjectId(projectId);
	 * 
	 * Project existingProject = projectRepository.findById(projectId)
	 * .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: "
	 * + projectId));
	 * 
	 * validateProject(project);
	 * 
	 * project.setProjectId(existingProject.getProjectId());
	 * 
	 * return projectRepository.save(project); }
	 */

	@Override
	public Project updateProject(Long projectId, Project project) {
		validateProjectId(projectId);

		if (project == null) {
			throw new IllegalArgumentException("Update data cannot be null");
		}

		Project existingProject = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

		boolean updated = false;

		if (project.getProjectName() != null) {
			String projectName = project.getProjectName().trim();

			if (projectName.isEmpty()) {
				throw new IllegalArgumentException("Project name cannot be empty");
			}

			existingProject.setProjectName(projectName);
			updated = true;
		}

		Long ownerId = project.getProjectOwnerId();

		if (ownerId != null && ownerId < 0) {
			throw new IllegalArgumentException("Project owner ID cannot be negative");
		}

		if (ownerId != null && ownerId > 0) {
			existingProject.setProjectOwnerId(ownerId);
			updated = true;
		}

		if (project.getStartDate() != null) {
			existingProject.setStartDate(project.getStartDate());
			updated = true;
		}

		if (project.getEndDate() != null) {
			existingProject.setEndDate(project.getEndDate());
			updated = true;
		}

		if (!updated) {
			return existingProject;
		}

		return projectRepository.save(existingProject);
	}

	/*
	 * @Override public void deleteProject(Long projectId) {
	 * 
	 * validateProjectId(projectId);
	 * 
	 * if (!projectRepository.existsById(projectId)) {
	 * 
	 * throw new ProjectNotFoundException("Project not found with id: " +
	 * projectId); }
	 * 
	 * projectRepository.deleteById(projectId); }
	 */

	@Override
	public List<IssueDto> getIssuesByProjectId(Long projectId) {

		validateProjectId(projectId);

		projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

		ResponseEntity<List<IssueDto>> response = issueFeign.getIssuesByProjectId(projectId);

		return response.getBody();
	}

	@Override
	public List<IssueDto> getIssuesByProjectName(String projectName) {

		if (projectName == null || projectName.isBlank()) {

			throw new IllegalArgumentException("Project name cannot be empty");
		}

		Project project = projectRepository.findByProjectName(projectName).stream().findFirst()
				.orElseThrow(() -> new ProjectNotFoundException("Project not found with name: " + projectName));

		Long projectId = project.getProjectId();

		ResponseEntity<List<IssueDto>> response = issueFeign.getIssuesByProjectId(projectId);

		return response.getBody();
	}

	@Override
	public List<Project> getProjectsByOwnerId(Long ownerId) {

		validateProjectOwnerId(ownerId);

		return projectRepository.findByProjectOwnerId(ownerId);
	}

	private void validateProject(Project project) {

		if (project == null) {

			throw new IllegalArgumentException("Project cannot be null");
		}

		validateProjectOwnerId(project.getProjectOwnerId());

		if (project.getProjectName() == null || project.getProjectName().isBlank()) {

			throw new IllegalArgumentException("Project name cannot be empty");
		}

		if (project.getStartDate() == null) {

			throw new IllegalArgumentException("Start date cannot be null");
		}

		if (project.getEndDate() == null) {

			throw new IllegalArgumentException("End date cannot be null");
		}

		if (project.getEndDate().isBefore(project.getStartDate())) {

			throw new IllegalArgumentException("End date cannot be before start date");
		}
	}

	private void validateProjectOwnerId(Long projectOwnerId) {

		if (projectOwnerId == null || projectOwnerId <= 0) {

			throw new IllegalArgumentException("Project owner ID must be greater than 0");
		}
	}

	private void validateProjectId(Long projectId) {

		if (projectId == null || projectId <= 0) {

			throw new IllegalArgumentException("Project ID must be greater than 0");
		}
	}
}