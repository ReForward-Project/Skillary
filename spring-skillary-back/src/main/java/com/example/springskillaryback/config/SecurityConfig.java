package com.example.springskillaryback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		// 요청 권한 설정
		return http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
				)
				.cors(cors -> cors.configurationSource(request -> {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(List.of("http://localhost:3000"));
					config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
					config.setAllowedHeaders(List.of("*"));
					config.setAllowCredentials(true);
					config.setExposedHeaders(List.of("Set-Cookie", "Authorization"));
					return config;
				}))
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.build();
	}
}