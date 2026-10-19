package com.deloitte.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.deloitte.dto.ProjectDto;

@FeignClient(
        name = "ProjectService",
        path = "/api/projects"
)
public interface ProjectFeign {

    @GetMapping("/owner/{ownerId}")
    ResponseEntity<List<ProjectDto>> getProjectsByOwnerId(
            @PathVariable("ownerId") Long ownerId);
}