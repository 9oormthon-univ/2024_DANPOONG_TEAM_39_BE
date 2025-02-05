package com.example._2024_danpoong_team_39_be.notification;
import com.example._2024_danpoong_team_39_be.domain.Member;
import com.example._2024_danpoong_team_39_be.login.repository.MemberRepository;
import com.example._2024_danpoong_team_39_be.login.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    @Autowired
    private final NotificationService notificationService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MemberRepository memberRepository;

     // 메시지 알림
     @GetMapping(value = "/api/notifications/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
     public SseEmitter subscribe(@RequestHeader(value = "Authorization") String token) {
         if (token == null || !token.startsWith("Bearer ")) {
             throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
         }

         String jwt = token.substring(7); // "Bearer " 이후의 실제 토큰 값
         String email = jwtUtil.getEmailFromToken(jwt); // JWT에서 이메일 추출

         if (email == null) {
             throw new IllegalArgumentException("유효한 이메일을 찾을 수 없습니다.");
         }

         // 이메일을 이용해 userId 조회
         Member member = memberRepository.findByEmail(email)
                 .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다."));

         Long userId = member.getId(); // Member 객체에서 ID 가져오기

         System.out.println("User " + userId + " subscribed"); // 구독 확인용 로그
         SseEmitter sseEmitter = notificationService.subscribe(userId);

         if (sseEmitter == null) {
             System.out.println("Failed to create SseEmitter for user " + userId);
         }

         return notificationService.subscribe(userId);
     }

    @GetMapping("/api/notifications")
    public ResponseEntity<?> getNotification(@RequestHeader(value = "Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            // JWT 토큰에서 실제 토큰 값 추출
            String jwt = token.substring(7); // "Bearer " 이후의 실제 토큰 값
            String email = null;

            try {
                email = jwtUtil.getEmailFromToken(jwt); // JWT에서 이메일 추출
                if (email == null) {
                    throw new IllegalArgumentException("유효한 이메일을 찾을 수 없습니다.");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 파싱 오류: " + e.getMessage());
            }

            try {
                // 이메일을 이용해 userId 조회
                Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다."));

                Long userId = member.getId();
                if (userId != null) {
                    // 사용자 ID를 기반으로 알림 정보 조회
                    System.out.println("알림 조회 중: 사용자 ID = " + userId); // 디버깅을 위한 로그
                    return ResponseEntity.ok(notificationService.findByMemberId(userId));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 ID를 찾을 수 없습니다.");
                }
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 이메일입니다.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알림 조회 중 오류 발생: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization 헤더가 잘못되었습니다.");
        }
    }








//    // 알림 삭제
//    @DeleteMapping("/api/notification/delete/{id}")
//    public MsgResponseDto deleteNotification(@PathVariable Long id) throws IOException {
//        return notificationService.deleteNotification(id);
//    }

}
