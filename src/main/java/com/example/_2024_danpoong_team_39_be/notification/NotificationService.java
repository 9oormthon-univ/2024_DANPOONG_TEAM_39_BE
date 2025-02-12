package com.example._2024_danpoong_team_39_be.notification;

import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import com.example._2024_danpoong_team_39_be.domain.Member;
import com.example._2024_danpoong_team_39_be.login.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환용 ObjectMapper

    public void sendJsonNotification(SseEmitter sseEmitter, Object data) {
        try {
            // ObjectMapper를 이용해 데이터를 JSON 형식으로 변환
            String jsonData = objectMapper.writeValueAsString(data);

            // JSON 데이터를 SSE로 전송
            sseEmitter.send(SseEmitter.event().name("message").data(jsonData));
        } catch (Exception e) {
            log.error("JSON 전송 중 오류 발생", e);
        }
    }
    /**
     * SSE 알림 구독
     * @param memberId 사용자의 ID
     * @return SseEmitter 인스턴스
     */
    public SseEmitter subscribe(Long memberId) {
        // 1. 현재 클라이언트를 위한 sseEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        // 3. 저장
        sseEmitters.put(memberId, sseEmitter);

        // 4. 연결 종료 처리
        sseEmitter.onCompletion(() -> sseEmitters.remove(memberId));    // sseEmitter 연결이 완료될 경우
        sseEmitter.onTimeout(() -> sseEmitters.remove(memberId));        // sseEmitter 연결에 타임아웃이 발생할 경우
        sseEmitter.onError((e) -> sseEmitters.remove(memberId));        // sseEmitter 연결에 오류가 발생할 경우

        return sseEmitter;
    }



    /**
     * 특정 회원의 알림 개수 조회
     * @param memberId 사용자 ID
     * @return 알림 개수
     */
    public int countNotificationsForMember(Long memberId) {
        List<Notification> notifications = notificationRepository.findByMemberId(memberId);
        return notifications.size();
    }


    /**
     * 캘린더 이벤트 발생 시 알림 전송
     * @param calendar 캘린더 이벤트 정보
     */
    @Transactional
    public void notifyCalendar(Calendar calendar) {
        List<Member> members = memberRepository.findAll();
        String data = String.format("[%s] 일정이 등록되었습니다.", calendar.getTitle());

        for (Member member : members) {
            SseEmitter sseEmitterReceiver = sseEmitters.get(member.getId());

            if (sseEmitterReceiver == null) {
                log.warn("SSE Emitter 없음. 회원ID={} (구독 필요)", member.getId());
                Notification notification = new Notification(member.getId(), data, "일정", calendar.getId());
                notificationRepository.save(notification);
                continue;
            }

            try {
                // JSON 형태로 전송할 데이터 준비
                Map<String, Object> notificationData = new HashMap<>();
                notificationData.put("message", data);
                notificationData.put("notificationCount", countNotificationsForMember(member.getId()));

                // JSON 데이터 전송
                sendJsonNotification(sseEmitterReceiver, notificationData);
            } catch (Exception e) {
                sseEmitters.remove(member.getId());
                log.error("알림 전송 실패. 회원ID={}", member.getId(), e);
            }
        }
    }

    public List<Notification> findByMemberId(Long userId) {
        try {
            log.info("알림 조회 중, 사용자 ID: {}", userId);
            List<Notification> notifications = notificationRepository.findByMemberId(userId);
            log.info("알림 조회 결과: {}", notifications);
            return notifications;
        } catch (Exception e) {
            log.error("알림 조회 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("알림 조회 중 오류 발생", e);
        }
    }
}
