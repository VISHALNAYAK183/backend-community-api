package com.community.backend.controller;

import com.community.backend.entity.User;
import com.community.backend.entity.Post;
import com.community.backend.repository.UserRepository;
import com.community.backend.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtUtil.extractUsername(token);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("bio", user.getBio());

        List<Map<String, Object>> posts = new ArrayList<>();
        for (Post post : user.getPosts()) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("content", post.getContent());
            postData.put("createdAt", post.getCreatedAt());
            posts.add(postData);
        }

        response.put("posts", posts);

        return ResponseEntity.ok(response);
    }
}
