package com.company.leave.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
/*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers("/manager/**").hasAuthority("ROLE_MANAGER")
            	    .requestMatchers("/employee/**").hasAuthority("ROLE_EMPLOYEE")
            	    .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
            	    .anyRequest().authenticated()
            	)

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    */
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()

                .requestMatchers("/employee/**")
                    .hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")

                .requestMatchers("/manager/**")
                    .hasRole("MANAGER")

                .requestMatchers("/admin/**")
                    .hasRole("ADMIN")

                .anyRequest().authenticated()
            )

            // ✅ THIS IS THE IMPORTANT PART
            .exceptionHandling(ex -> ex

                // 🔴 401 – Not authenticated / invalid token
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");

                    response.getWriter().write("""
                    {
                      "status": 401,
                      "error": "UNAUTHORIZED",
                      "message": "Authentication required or token is invalid",
                      "path": "%s"
                    }
                    """.formatted(request.getRequestURI()));
                })

                // 🔴 403 – Authenticated but no permission
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");

                    response.getWriter().write("""
                    {
                      "status": 403,
                      "error": "FORBIDDEN",
                      "message": "You do not have permission to access this resource",
                      "path": "%s"
                    }
                    """.formatted(request.getRequestURI()));
                })
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
