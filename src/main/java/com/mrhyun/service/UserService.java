package com.mrhyun.service;

import com.mrhyun.entity.User;
import com.mrhyun.entity.UserRole;
import com.mrhyun.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("사용자 정보 로드 시도: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("사용자를 찾을 수 없음: {}", username);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
                });
    }

    @Transactional
    public User register(String username, String name, String email, String password, String phoneNumber) {
        log.info("회원가입 처리 시작: {}", username);
        
        // 사용자 이름 중복 체크
        if (userRepository.existsByUsername(username)) {
            log.warn("회원가입 실패: 사용자 이름 중복 - {}", username);
            throw new RuntimeException("이미 사용 중인 사용자 이름입니다.");
        }

        // 이메일 중복 체크
        if (userRepository.existsByEmail(email)) {
            log.warn("회원가입 실패: 이메일 중복 - {}", email);
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        // 새 사용자 생성
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);
        user.setRole(UserRole.USER);

        // 사용자 저장
        User savedUser = userRepository.save(user);
        log.info("회원가입 성공: {}", username);
        return savedUser;
    }

    @Transactional
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("인증되지 않은 사용자");
            throw new RuntimeException("로그인이 필요합니다");
        }
        String username = authentication.getName();
        log.info("현재 사용자 정보 조회: {}", username);
        return getUserByUsername(username);
    }

    public User getUserByUsername(String username) {
        log.info("사용자 정보 조회: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("사용자를 찾을 수 없음: {}", username);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
                });
    }
    
    @Transactional
    public void changePassword(String currentPassword, String newPassword) {
        log.info("비밀번호 변경 처리 시작");
        
        User currentUser = getCurrentUser();
        
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
            log.warn("비밀번호 변경 실패: 현재 비밀번호 불일치");
            throw new RuntimeException("현재 비밀번호가 올바르지 않습니다");
        }
        
        // 새 비밀번호 설정
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
        
        log.info("비밀번호 변경 성공: {}", currentUser.getUsername());
    }
    
    @Transactional
    public void deleteAccount(String password) {
        log.info("계정 삭제 처리 시작");
        
        User currentUser = getCurrentUser();
        
        // 비밀번호 확인
        if (!passwordEncoder.matches(password, currentUser.getPassword())) {
            log.warn("계정 삭제 실패: 비밀번호 불일치");
            throw new RuntimeException("비밀번호가 올바르지 않습니다");
        }
        
        // 계정 삭제
        userRepository.delete(currentUser);
        
        // 보안 컨텍스트 초기화
        SecurityContextHolder.clearContext();
        
        log.info("계정 삭제 성공: {}", currentUser.getUsername());
    }
} 