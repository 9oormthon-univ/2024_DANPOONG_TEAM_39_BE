package com.example._2024_danpoong_team_39_be.careCalendar.hospital;


import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransportationTpye transportationTpye; // 이동 방식 (예: 자가용, 택시 등)

    private boolean caregiver;
    // Calendar와의 관계 설정
    @OneToOne
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;
}
