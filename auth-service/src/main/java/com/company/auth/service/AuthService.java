package com.company.auth.service;

import org.springframework.stereotype.Service;

import com.company.auth.auth.User;
import com.company.auth.dto.LoginRequest;
import com.company.auth.dto.LoginResponse;
import com.company.auth.repository.UserRepository;
import com.company.auth.utility.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getEmployeeId(),
                user.getRole()
        );

        return new LoginResponse(
        		token,
        		user.getEmployeeId(),
        		user.getRole()
        		);
    }
}


