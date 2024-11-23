package com.example._2024_danpoong_team_39_be.careCalendar.others;


import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Others {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean caregiver;
    @OneToOne
    @JoinColumn(name = "calendar_id")  // This will fillUpMember the "Calendar" entity
    private Calendar calendar;
}
