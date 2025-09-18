package com.loanmaker.controller;

import com.loanmaker.model.User;
import com.loanmaker.service.UserService;
import com.loanmaker.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // ✅ User registration
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Set default credit score if not provided
        if (user.getCreditScore() == null) {
            user.setCreditScore(0);
        }
        return userService.registerUser(user);
    }

    // ✅ Admin registration
    @PostMapping("/register-admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User registerAdmin(@RequestBody User user) {
        if (user.getCreditScore() == null) {
            user.setCreditScore(0);
        }
        return userService.registerAdmin(user);
    }

    // ✅ Get all users (Admin only)
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ Get logged-in user profile based on JWT token
    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Hide password for security
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    // ✅ Update logged-in user profile
    @PutMapping("/profile")
    @PreAuthorize("hasAnyAuthority('USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> updateUserProfile(@RequestBody User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update allowed fields
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getPhone() != null) {
            user.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getCreditScore() != null) {
            user.setCreditScore(updatedUser.getCreditScore());
        }

        User savedUser = userRepository.save(user);
        savedUser.setPassword(null); // hide password
        return ResponseEntity.ok(savedUser);
    }

    // ✅ Admin-only stats endpoint
    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAdminStats() {
        long totalUsers = userRepository.count();
        long totalAdmins = userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().equalsIgnoreCase("ROLE_ADMIN"))
                .count();
        long totalNormalUsers = totalUsers - totalAdmins;

        return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
            put("totalUsers", totalUsers);
            put("totalAdmins", totalAdmins);
            put("totalNormalUsers", totalNormalUsers);
        }});
    }
}
