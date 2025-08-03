package com.community.backend.service;

import com.community.backend.dto.LoginRequest;
import com.community.backend.dto.RegisterRequest;
import com.community.backend.entity.User;
import com.community.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;   
           
    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBio(request.getBio());

        userRepository.save(user);
        return "User registered successfully!";
    }
      public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

      if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
    throw new RuntimeException("Invalid credentials");
}

        return jwtService.generateToken(user);
    }
}
