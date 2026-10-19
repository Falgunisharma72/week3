package com.deloitte.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.deloitte.dto.IssueDto;

@FeignClient(name = "IssueService", path = "/api/issues")
public interface IssueFeign {

	@GetMapping("/project/{projectId}")
	ResponseEntity<List<IssueDto>> getIssuesByProjectId(@PathVariable("projectId") Long projectId);
}