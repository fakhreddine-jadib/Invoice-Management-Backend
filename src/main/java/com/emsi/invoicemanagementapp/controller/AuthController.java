package com.emsi.invoicemanagementapp.controller;

import com.emsi.invoicemanagementapp.dto.LoginRequest;
import com.emsi.invoicemanagementapp.dto.LoginResponse; // <-- IMPORT
import com.emsi.invoicemanagementapp.dto.RegistrationRequest;
import com.emsi.invoicemanagementapp.model.User;
import com.emsi.invoicemanagementapp.repository.UserRepository;
import com.emsi.invoicemanagementapp.security.JwtUtils; // <-- IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // <-- IMPORT
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils; // <-- ADD DEPENDENCY

    // POST http://localhost:8081/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String token = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    // POST http://localhost:8081/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setRole(registrationRequest.getRole());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }
}