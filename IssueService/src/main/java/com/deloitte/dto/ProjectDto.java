package com.deloitte.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDto {

    private Long projectId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long projectOwnerId;
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public Long getProjectOwnerId() {
		return projectOwnerId;
	}
	public void setProjectOwnerId(Long projectOwnerId) {
		this.projectOwnerId = projectOwnerId;
	}
    
    
}