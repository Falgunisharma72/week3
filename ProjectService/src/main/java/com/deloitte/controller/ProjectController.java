package com.deloitte.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deloitte.dto.IssueDto;
import com.deloitte.entity.Project;
import com.deloitte.exception.ProjectNotFoundException;
import com.deloitte.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ProjectService projectService;

	@GetMapping
	public ResponseEntity<List<Project>> getAllProjects() {

		try {

			logger.info("Fetching all projects");

			List<Project> projects = projectService.getAllProjects();

			return ResponseEntity.status(HttpStatus.OK).body(projects);

		} catch (Exception e) {

			logger.error("Failed to fetch all projects", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<Project> getProjectById(@PathVariable("projectId") Long projectId) {

		try {

			logger.info("Fetching project with id: {}", projectId);

			Optional<Project> project = projectService.getProjectById(projectId);

			return ResponseEntity.status(HttpStatus.OK).body(project.get());

		} catch (ProjectNotFoundException e) {

			throw e;

		} catch (IllegalArgumentException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to get project with id: {}", projectId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping
	public ResponseEntity<Project> createProject(@RequestBody Project project) {

		try {

			logger.info("Creating project: {}", project.getProjectName());

			Project createdProject = projectService.saveProject(project);

			return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);

		} catch (IllegalArgumentException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to create project", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/{projectId}")
	public ResponseEntity<Project> updateProject(@PathVariable("projectId") Long projectId,
			@RequestBody Project project) {

		try {

			logger.info("Updating project with id: {}", projectId);

			Project updatedProject = projectService.updateProject(projectId, project);

			return ResponseEntity.status(HttpStatus.OK).body(updatedProject);

		} catch (ProjectNotFoundException e) {

			throw e;

		} catch (IllegalArgumentException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to update project with id: {}", projectId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/*
	 * @DeleteMapping("/{projectId}") public ResponseEntity<Void>
	 * deleteProject(@PathVariable("projectId") Long projectId) {
	 * 
	 * try {
	 * 
	 * logger.info("Deleting project with id: {}", projectId);
	 * 
	 * projectService.deleteProject(projectId);
	 * 
	 * return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	 * 
	 * } catch (ProjectNotFoundException e) {
	 * 
	 * throw e;
	 * 
	 * } catch (IllegalArgumentException e) {
	 * 
	 * throw e;
	 * 
	 * } catch (Exception e) {
	 * 
	 * logger.error("Failed to delete project with id: {}", projectId, e);
	 * 
	 * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); } }
	 */

	@GetMapping("/{projectId}/issues")
	public ResponseEntity<List<IssueDto>> getIssuesByProjectId(@PathVariable("projectId") Long projectId) {

		try {

			logger.info("Fetching issues for project id: {}", projectId);

			List<IssueDto> issues = projectService.getIssuesByProjectId(projectId);

			return ResponseEntity.status(HttpStatus.OK).body(issues);

		} catch (ProjectNotFoundException e) {

			throw e;

		} catch (IllegalArgumentException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to fetch issues for project id: {}", projectId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/projectName/{projectName}/issues")
	public ResponseEntity<List<IssueDto>> getIssuesByProjectName(@PathVariable("projectName") String projectName) {

		try {

			logger.info("Fetching issues for project name: {}", projectName);

			List<IssueDto> issues = projectService.getIssuesByProjectName(projectName);

			return ResponseEntity.status(HttpStatus.OK).body(issues);

		} catch (ProjectNotFoundException e) {

			throw e;

		} catch (IllegalArgumentException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to fetch issues for project name: {}", projectName, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/owner/{ownerId}")
	public ResponseEntity<List<Project>> getProjectsByOwnerId(@PathVariable("ownerId") Long ownerId) {

		try {

			logger.info("Fetching projects for owner id: {}", ownerId);

			List<Project> projects = projectService.getProjectsByOwnerId(ownerId);

			return ResponseEntity.status(HttpStatus.OK).body(projects);

		} catch (IllegalArgumentException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to fetch projects for owner id: {}", ownerId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}