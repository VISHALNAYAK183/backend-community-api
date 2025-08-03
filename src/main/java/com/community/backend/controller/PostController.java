package com.community.backend.controller;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.community.backend.entity.User; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.community.backend.dto.PostRequest;
import com.community.backend.entity.Post;
import com.community.backend.repository.PostRepository;
import com.community.backend.repository.UserRepository;
import com.community.backend.util.JwtUtil;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtUtil.extractUsername(token);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setContent(postRequest.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(user);

        postRepository.save(post);

        return ResponseEntity.ok("Post created successfully");
    }


    @GetMapping
public ResponseEntity<?> getAllPosts() {
    List<Post> posts = postRepository.findAll();

    List<Map<String, Object>> postResponses = posts.stream().map(post -> {
        Map<String, Object> map = new HashMap<>();
        map.put("id", post.getId());
        map.put("content", post.getContent());
        map.put("createdAt", post.getCreatedAt());
        
        User user = post.getUser();
        if (user != null) {
            map.put("userEmail", user.getEmail());
            map.put("userName", user.getName()); // if name exists
        }

        return map;
    }).collect(Collectors.toList());

    return ResponseEntity.ok(postResponses);
}

}
