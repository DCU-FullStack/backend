package com.chill.backend.controller;

import com.chill.backend.model.User;
import com.chill.backend.security.JwtTokenProvider;
import com.chill.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");
            String phoneNumber = request.get("phoneNumber");
            String name = request.get("name");

            // Check for phone_number field if phoneNumber is not present
            if ((phoneNumber == null || phoneNumber.isEmpty()) && request.containsKey("phone_number")) {
                phoneNumber = request.get("phone_number");
            }

            if (phoneNumber == null || phoneNumber.isEmpty()) {
                throw new RuntimeException("Phone number is required");
            }

            if (name == null || name.isEmpty()) {
                throw new RuntimeException("Name is required");
            }

            // 모든 필수 필드가 있는지 확인
            if (username == null || username.isEmpty()) {
                throw new RuntimeException("Username is required");
            }
            if (email == null || email.isEmpty()) {
                throw new RuntimeException("Email is required");
            }
            if (password == null || password.isEmpty()) {
                throw new RuntimeException("Password is required");
            }

            User user = userService.register(username, email, password, phoneNumber, name);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Registration successful");
            response.put("user", user);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            User user = userService.findByUsername(username);
            
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid password");
            }
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            
            String token = jwtTokenProvider.createToken(authentication);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("user", user);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid username or password");
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> request) {
        try {
            Long userId = Long.parseLong(request.get("userId"));
            String name = request.get("name");
            String email = request.get("email");
            String phoneNumber = request.get("phone_number");

            User updatedUser = userService.updateProfile(userId, name, email, phoneNumber);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile updated successfully");
            response.put("user", updatedUser);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        try {
            Long userId = Long.parseLong(request.get("userId"));
            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");
            String confirmNewPassword = request.get("confirmNewPassword");

            if (!newPassword.equals(confirmNewPassword)) {
                throw new RuntimeException("New passwords do not match");
            }

            User updatedUser = userService.changePassword(userId, currentPassword, newPassword);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Password changed successfully");
            response.put("user", updatedUser);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody Map<String, String> request) {
        try {
            Long userId = Long.parseLong(request.get("userId"));
            String password = request.get("password");

            User user = userService.findById(userId);
            
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid password");
            }

            userService.deleteAccount(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Account deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
} 