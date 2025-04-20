package com.mrhyun.controller;

import com.mrhyun.entity.User;
import com.mrhyun.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Data
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        log.info("회원가입 요청: {}", request.getUsername());
        
        // 비밀번호 확인
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            log.warn("비밀번호가 일치하지 않음: {}", request.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "비밀번호가 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            User user = userService.register(
                request.getUsername(),
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhoneNumber()
            );
            log.info("회원가입 성공: {}", user.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "회원가입이 완료되었습니다.");
            response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "name", user.getName(),
                "email", user.getEmail(),
                "phoneNumber", user.getPhoneNumber(),
                "role", user.getRole()
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("회원가입 실패: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        log.info("로그인 요청 받음: {}", request.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            User loggedInUser = (User) authentication.getPrincipal();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "로그인 성공");
            response.put("user", Map.of(
                "id", loggedInUser.getId(),
                "username", loggedInUser.getUsername(),
                "name", loggedInUser.getName(),
                "email", loggedInUser.getEmail(),
                "phoneNumber", loggedInUser.getPhoneNumber(),
                "role", loggedInUser.getRole()
            ));
            
            log.info("로그인 성공: {}", loggedInUser.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("로그인 실패: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "아이디 또는 비밀번호가 올바르지 않습니다");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        log.info("로그아웃 요청 받음");
        SecurityContextHolder.clearContext();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "로그아웃 성공");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            User currentUser = userService.getCurrentUser();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", Map.of(
                "id", currentUser.getId(),
                "username", currentUser.getUsername(),
                "name", currentUser.getName(),
                "email", currentUser.getEmail(),
                "phoneNumber", currentUser.getPhoneNumber(),
                "role", currentUser.getRole()
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("현재 사용자 정보 조회 실패: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        log.info("비밀번호 변경 요청 받음");
        
        try {
            userService.changePassword(
                request.getCurrentPassword(),
                request.getNewPassword()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "비밀번호가 성공적으로 변경되었습니다");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("비밀번호 변경 실패: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody DeleteAccountRequest request) {
        log.info("계정 삭제 요청 받음");
        
        try {
            userService.deleteAccount(request.getPassword());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "계정이 성공적으로 삭제되었습니다");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("계정 삭제 실패: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

@Data
class RegisterRequest {
    @NotBlank(message = "사용자 이름은 필수입니다")
    private String username;

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이어야 합니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다")
    private String confirmPassword;

    private String phoneNumber;
}

@Data
class LoginRequest {
    private String username;
    private String password;
}

@Data
class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
}

@Data
class DeleteAccountRequest {
    private String password;
}

@lombok.Data
@lombok.AllArgsConstructor
class RegisterResponse {
    private Long id;
    private String username;
} 