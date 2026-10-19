package com.deloitte.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IssueDto {

	private Long issueId;
	private String summary;
	private Long projectId;
	private String description;
	private String priority;
	private Long assigneeId;
	private String status;
	private LocalDate createdOn;
	private LocalDate lastUpdated;
	private String tag;
	private String sprint;
	private Integer storyPoint;
	private String type;
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
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public Long getAssigneeId() {
		return assigneeId;
	}
	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}