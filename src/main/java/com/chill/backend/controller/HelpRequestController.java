package com.chill.backend.controller;

import com.chill.backend.dto.HelpRequestDTO;
import com.chill.backend.model.HelpRequestEntity;
import com.chill.backend.repository.HelpRequestRepository;
import com.chill.backend.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // 리액트 포트 번호 맞춤
public class HelpRequestController {

    private final HelpRequestRepository helpRequestRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 생성자 주입
    public HelpRequestController(HelpRequestRepository helpRequestRepository, JwtTokenProvider jwtTokenProvider) {
        this.helpRequestRepository = helpRequestRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/help")
    public ResponseEntity<String> receiveQuestion(
            @RequestHeader("Authorization")
            String authorizationHeader,  // <<< 토큰을 헤더에서 바로 받자
            @RequestBody HelpRequestDTO helpRequestDTO) {

        // Bearer 제거하고 토큰만 파싱
        String token = authorizationHeader.replace("Bearer ", "");
        System.out.println("받은 토큰: " + token);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("✅ 토큰 검증 성공");
        } else {
            System.out.println("❌ 토큰 검증 실패");
            return ResponseEntity.status(403).body("인증 실패");
        }

        System.out.println("문의 도착!");
        System.out.println("이름: " + helpRequestDTO.getName());
        System.out.println("이메일: " + helpRequestDTO.getEmail());
        System.out.println("제목: " + helpRequestDTO.getSubject());
        System.out.println("내용: " + helpRequestDTO.getMessage());

        // DTO -> Entity 변환
        HelpRequestEntity helpRequestEntity = new HelpRequestEntity(
                helpRequestDTO.getName(),
                helpRequestDTO.getEmail(),
                helpRequestDTO.getSubject(),
                helpRequestDTO.getMessage(),
                helpRequestDTO.getCreatedAt(),
                helpRequestDTO.getStatus()
        );

        helpRequestRepository.save(helpRequestEntity);

        return ResponseEntity.ok("문의가 성공적으로 접수되었습니다.");
    }

    @GetMapping("/help/inquiries")
    public ResponseEntity<?> getAllInquiries(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // DB에서 createdAt 내림차순으로 정렬된 목록을 가져온다
            List<HelpRequestEntity> inquiries = helpRequestRepository.findAllByOrderByCreatedAtDesc();

            List<HelpRequestDTO> inquiriesDTO = inquiries.stream()
                    .map(entity -> new HelpRequestDTO(
                            entity.getName(),
                            entity.getEmail(),
                            entity.getSubject(),
                            entity.getMessage(),
                            entity.getCreatedAt(),
                            entity.getStatus() != null ? entity.getStatus() : "대기중"
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(inquiriesDTO);
        } else {
            return ResponseEntity.status(403).body("인증 실패");
        }
    }

}
