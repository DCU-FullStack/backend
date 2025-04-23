package com.chill.backend.controller;

import com.chill.backend.model.User;
import com.chill.backend.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        try {
            User updatedUser = userService.changePassword(
                request.getUserId(),
                request.getCurrentPassword(),
                request.getNewPassword()
            );
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

@Data
class PasswordChangeRequest {
    private Long userId;
    private String currentPassword;
    private String newPassword;
} 