package com.deloitte.service;

import java.util.List;
import java.util.Optional;

import com.deloitte.dto.IssueDto;
import com.deloitte.dto.LoginDto;
import com.deloitte.entity.User;

public interface UserService {

	List<User> getAllUsers();

	Optional<User> getUserById(Long userId);

	User saveUser(User user);

	User login(LoginDto dto);

	List<IssueDto> getIssuesByUserId(Long userId);
	
	List<IssueDto> getIssuesByUsername(String username);
}