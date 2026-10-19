package com.deloitte.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// Handles UserNotFoundException
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException exception) {

		logger.warn("{}", exception.getMessage());

		Map<String, String> errorResponse = Map.of("error", "Not Found", "message", exception.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	// Handles IllegalArgumentException
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException exception) {

		logger.warn("{}", exception.getMessage());

		Map<String, String> errorResponse = Map.of("error", "Bad Request", "message", exception.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	// Handles all other unexpected exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleException(Exception exception) {

		logger.error("Unhandled exception occurred", exception);

		Map<String, String> errorResponse = Map.of("error", "Internal Server Error", "message",
				"An unexpected error occurred.");

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}