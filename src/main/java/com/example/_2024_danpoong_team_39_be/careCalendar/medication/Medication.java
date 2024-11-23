package com.example._2024_danpoong_team_39_be.careCalendar.medication;


import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicationType; // 약 종류 (예: 항생제, 진통제 등)
    private boolean caregiver;
    // Calendar와의 관계 설정
    @OneToOne
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;
}
