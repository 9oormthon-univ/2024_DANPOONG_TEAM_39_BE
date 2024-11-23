package com.example._2024_danpoong_team_39_be.careCalendar.rest;


import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Rest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RestType restType; // 휴식 종류 (예: 낮잠, 휴식 등)
    private boolean caregiver;
    // Calendar와의 관계 설정
    @OneToOne
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;
}
