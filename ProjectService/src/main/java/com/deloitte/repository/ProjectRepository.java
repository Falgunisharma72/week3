package com.deloitte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deloitte.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

	List<Project> findByProjectOwnerId(Long projectOwnerId);

	List<Project> findByProjectName(String projectName);
}