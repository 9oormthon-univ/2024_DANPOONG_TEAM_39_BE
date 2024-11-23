package com.example._2024_danpoong_team_39_be.careCalendar.meal;


import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Use EnumType.STRING to persist the enum as a string in the database
    private MealType mealType; // 식사 타입 (예: 아침, 점심, 저녁)

    private boolean caregiver;
    // Calendar와의 관계 설정
    @OneToOne
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;
}
