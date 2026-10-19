package com.deloitte.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Lets the React frontend (a different origin, e.g. http://localhost:3000)
 * call the gateway from the browser. Add more origins to the list below if needed.
 */
@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {

		List<String> origins = List.of("http://localhost:3000", "http://localhost:3001", "http://127.0.0.1:3000");

		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(origins);
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", config);
		return new CorsFilter(source);
	}
}
