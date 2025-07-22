package com.gpt.meetingnotes.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	 @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        http
	            .cors().configurationSource(corsConfigurationSource())
	            .and()
	            .csrf().ignoringAntMatchers("/api/**")
	            .and()
	            .headers()
	                .xssProtection().block(true)
	                .and()
	                .contentSecurityPolicy("script-src 'self'")
	                .and()
	                .frameOptions().sameOrigin()
	            .and()
	            .authorizeRequests()
	                .anyRequest().permitAll();

	        return http.build();
	    }

	    @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration config = new CorsConfiguration();
	        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://your-prod.com", "http://localhost:8080"));
	        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
	        config.setAllowCredentials(true);
	        config.setMaxAge(3600L);

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);
	        return source;
	    }
}
