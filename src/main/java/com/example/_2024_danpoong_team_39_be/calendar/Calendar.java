package com.example._2024_danpoong_team_39_be.calendar;


import com.example._2024_danpoong_team_39_be.careCalendar.hospital.Hospital;
import com.example._2024_danpoong_team_39_be.careCalendar.meal.Meal;
import com.example._2024_danpoong_team_39_be.careCalendar.medication.Medication;
import com.example._2024_danpoong_team_39_be.careCalendar.others.Others;
import com.example._2024_danpoong_team_39_be.careCalendar.rest.Rest;
import com.example._2024_danpoong_team_39_be.domain.CareAssignment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
@Entity

public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    @Column(nullable = false)
    private String title; // 일정 제목
    private String eventType; // 일정타입 공부, 팀플 등등
    @Column(nullable = false)
    private LocalTime startTime; // 시작 시간
    @Column(nullable = false)
    private LocalTime endTime; // 종료 시간
    @Column(nullable = false)
    private LocalDate date;// 날짜
    @Enumerated(EnumType.STRING)
    private RepeatCycle repeatCycle;
    private Boolean isAllday;
    private Boolean isAlarm; // 알림 여부
    private String location; // 위치
    private String memo; // 메모
    private Boolean isShared; // 공유 여부
    // Enum 클래스: 반복 주기 정의
    public enum RepeatCycle {
        DAILY, WEEKLY, MONTHLY
    }

    /// 카테고리별 추가 정보 (식사, 병원, 휴식, 복약, 내일정)
    @OneToOne(mappedBy = "calendar", cascade = CascadeType.ALL)
    private Meal meal;

    @OneToOne(mappedBy = "calendar", cascade = CascadeType.ALL)
    private Hospital hospital;

    @OneToOne(mappedBy = "calendar", cascade = CascadeType.ALL)
    private Rest rest;

    @OneToOne(mappedBy = "calendar", cascade = CascadeType.ALL)
    private Medication medication;

    @OneToOne(mappedBy = "calendar", cascade = CascadeType.ALL)
    private Others others;

    @OneToOne(mappedBy = "calendar", cascade = CascadeType.ALL)
    private MyCalendar myCalendar;

    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_assignment_id")
    @JsonIgnore
    private CareAssignment careAssignment; // CareAssignment와의 관계

    //돌보미 등록
    private String name;


}
