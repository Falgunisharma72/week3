package com.deloitte.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.dto.IssueDto;
import com.deloitte.dto.LoginDto;
import com.deloitte.entity.User;
import com.deloitte.exception.UserNotFoundException;
import com.deloitte.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<List<User>> getAllTheUsers() {

		try {

			List<User> users = userService.getAllUsers();

			return ResponseEntity.status(HttpStatus.OK).body(users);

		} catch (Exception e) {

			logger.error("Failed to fetch users", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {

		try {

			logger.info("Creating user with email: {}", user.getEmail());

			User createdUser = userService.saveUser(user);

			return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

		} catch (Exception e) {

			logger.error("Failed to create user", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody LoginDto loginDto) {

		try {

			User existingUser = userService.login(loginDto);

			return ResponseEntity.status(HttpStatus.OK).body(existingUser);

		} catch (UserNotFoundException e) {

			throw e;

		} catch (RuntimeException e) {

			logger.warn("User login failed: {}", e.getMessage());

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		} catch (Exception e) {

			logger.error("Failed to login user", e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Long userId) {

		try {

			Optional<User> user = userService.getUserById(userId);

			if (user.isEmpty()) {

				throw new UserNotFoundException("User not found with id: " + userId);
			}

			return ResponseEntity.status(HttpStatus.OK).body(user.get());

		} catch (UserNotFoundException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to get user with id: {}", userId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{userId}/issues")
	public ResponseEntity<List<IssueDto>> getUserIssues(@PathVariable Long userId) {

		try {

			logger.info("Fetching issues for user id: {}", userId);

			List<IssueDto> issues = userService.getIssuesByUserId(userId);

			return ResponseEntity.status(HttpStatus.OK).body(issues);

		} catch (UserNotFoundException e) {

			throw e;

		} catch (Exception e) {

			logger.error("Failed to fetch issues for user id: {}", userId, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/username/{username}/issues")
	public ResponseEntity<List<IssueDto>> getUserIssuesByUsername(@PathVariable("username") String username) {
		try {
			logger.info("Fetching issues for user with username: {}", username);

			List<IssueDto> issues = userService.getIssuesByUsername(username);

			return ResponseEntity.status(HttpStatus.OK).body(issues);

		} catch (UserNotFoundException e) {
			throw e;

		} catch (Exception e) {
			logger.error("Failed to fetch issues for user with username: {}", username, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}