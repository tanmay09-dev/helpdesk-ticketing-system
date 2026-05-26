package com.tanmay.helpdesk_backend.controller;

import java.util.List;

import com.tanmay.helpdesk_backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tanmay.helpdesk_backend.JwtUtil;
import com.tanmay.helpdesk_backend.entity.User;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

public class TestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // TEST API
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Backend 🚀";
    }

    // =========================
    // SIGNUP API
    // =========================
    @PostMapping("/auth/signup")
    public String createUser(@RequestBody User user) {

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            return "User already exists";
        }

        // password encode
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        return "User created successfully";
    }

    // =========================
    // LOGIN API
    // =========================
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        boolean isMatch = passwordEncoder.matches(
                user.getPassword(),
                existingUser.getPassword()
        );

        if (isMatch) {

            String token = jwtUtil.generateToken(existingUser.getEmail(),existingUser.getRole());

            return ResponseEntity.ok(token);

        } else {

            return ResponseEntity.status(401).body("Wrong Password");
        }
    }

    // =========================
    // GET ALL USERS
    // =========================
    @GetMapping("/users")
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    // =========================
    // DELETE USER
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

           String role = authentication.getAuthorities().toString();

          System.out.println(role);

        userRepository.deleteById(id);

        return "User deleted successfully";
    }

    // =========================
    // UPDATE USER
    // =========================
    @PutMapping("/users/{id}")
    public User updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) {

        User user = userRepository.findById(id).orElseThrow();

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());

        // encode updated password
        user.setPassword(
                passwordEncoder.encode(updatedUser.getPassword())
        );

        return userRepository.save(user);
    }
}