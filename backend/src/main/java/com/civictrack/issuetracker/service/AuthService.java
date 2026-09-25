package com.civictrack.issuetracker.service;

import com.civictrack.issuetracker.dto.AuthResponse;
import com.civictrack.issuetracker.dto.LoginRequest;
import com.civictrack.issuetracker.dto.RegisterRequest;
import com.civictrack.issuetracker.entity.Role;
import com.civictrack.issuetracker.entity.User;
import com.civictrack.issuetracker.exception.ApiException;
import com.civictrack.issuetracker.repository.UserRepository;
import com.civictrack.issuetracker.security.AppUserPrincipal;
import com.civictrack.issuetracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "An account with this email already exists");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.CITIZEN)
                .build();
        user = userRepository.save(user);

        AppUserPrincipal principal = new AppUserPrincipal(user);
        String token = jwtService.generateToken(principal, user.getId(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        AppUserPrincipal principal = new AppUserPrincipal(user);
        String token = jwtService.generateToken(principal, user.getId(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
