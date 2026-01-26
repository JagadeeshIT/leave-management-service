package com.company.leave.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
	private CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtFilter = jwtFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(ex -> ex
                .accessDeniedHandler(customAccessDeniedHandler) // 🔥 THIS LINE
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/employee/**").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                .requestMatchers("/manager/**").hasRole("MANAGER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
