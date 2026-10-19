package com.deloitte.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.entity.Issue;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

	List<Issue> findByProjectId(Long projectId);

	List<Issue> findByAssigneeId(Long assigneeId);
}