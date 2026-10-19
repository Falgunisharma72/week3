package com.deloitte.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "issues")
public class Issue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long issueId;

	@Column(nullable = false)
	private String summary;

	@Column(nullable = false)
	private Long projectId;

	@Column(nullable = false)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Priority priority;

	@Column(nullable = false)
	private Long assigneeId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@Column(nullable = false)
	private LocalDate createdOn;

	@Column(nullable = false)
	private LocalDate lastUpdated;

	@Column(nullable = false)
	private String tag;

	@Column(nullable = false)
	private String sprint;

	@Column(nullable = false)
	private Integer storyPoint;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private IssueType type;

	public enum Priority {
		HIGH, MEDIUM, LOW
	}

	public enum Status {
		TO_DO, TESTING, DEVELOPMENT, COMPLETED
	}

	public enum IssueType {
		BUG, FEATURE, TASK
	}

	public Long getIssueId() {
		return issueId;
	}

	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDate getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDate createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDate getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDate lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSprint() {
		return sprint;
	}

	public void setSprint(String sprint) {
		this.sprint = sprint;
	}

	public Integer getStoryPoint() {
		return storyPoint;
	}

	public void setStoryPoint(Integer storyPoint) {
		this.storyPoint = storyPoint;
	}

	public IssueType getType() {
		return type;
	}

	public void setType(IssueType type) {
		this.type = type;
	}
	
	
}