package com.deloitte.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.deloitte.client.IssueFeign;
import com.deloitte.dto.IssueDto;
import com.deloitte.dto.LoginDto;
import com.deloitte.entity.User;
import com.deloitte.exception.UserNotFoundException;
import com.deloitte.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private IssueFeign issueFeign;

	@Override
	public List<User> getAllUsers() {

		return userRepository.findAll();
	}

	@Override
	public Optional<User> getUserById(Long userId) {

		return userRepository.findById(userId);
	}

	@Override
	public User saveUser(User user) {

		return userRepository.save(user);
	}

	@Override
	public User login(LoginDto dto) {

		User user = userRepository.findByEmail(dto.getEmail())
				.orElseThrow(() -> new UserNotFoundException("Invalid email"));

		if (!user.getPassword().equals(dto.getPassword())) {

			throw new IllegalArgumentException("Invalid password");
		}

		return user;
	}

	@Override
	public List<IssueDto> getIssuesByUserId(Long userId) {

		userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

		ResponseEntity<List<IssueDto>> response = issueFeign.getIssuesByAssignee(userId);

		return response.getBody();
	}

	@Override
	public List<IssueDto> getIssuesByUsername(String username) {
		User user = userRepository.findByName(username)
				.orElseThrow(() -> new UserNotFoundException("User not found with username " + username));

		ResponseEntity<List<IssueDto>> response = issueFeign.getIssuesByAssignee(user.getUserId());
		return response.getBody();
	}
}