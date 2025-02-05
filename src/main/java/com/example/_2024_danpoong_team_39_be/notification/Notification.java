package com.example._2024_danpoong_team_39_be.notification;

import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Entity
@Getter @Setter @ToString
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String message;

    private String type;

    @CreatedDate
    private LocalDateTime createdDate;

    private Long calendarId;
    private Long couponId;
    public Notification(Long memberId, String message, String type, Long id) {
        switch (type) {
            case "일정":
                this.calendarId = id;
                break;
            case "쿠폰":
                this.couponId = id;
                break;
            default:
                throw new IllegalArgumentException("공지 타입 입력 오류= " + type);
        }
        this.memberId = memberId;
        this.message = message;
        this.type = type;
        this.createdDate = LocalDateTime.now();
    }

}
