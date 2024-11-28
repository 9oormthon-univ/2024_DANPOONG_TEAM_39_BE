package com.example._2024_danpoong_team_39_be.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50)
    private String diagnosis;

    @Column(length = 50)
    private String mobilityStatus;

    private LocalTime startSleepTime;

    private LocalTime endSleepTime;

    private String address;

    private LocalTime avgSleepTime;

    // 연관관계
    @OneToMany(mappedBy = "recipient")
    private List<CareAssignment> careAssignment = new ArrayList<>();

    // 프로필 이미지 없습니다! (추후 추가 예정)

    public CareRecipient(
            Long id, String name, String diagnosis, String mobilityStatus, LocalTime startSleepTime, LocalTime endSleepTime, String address, LocalTime avgSleepTime) {
        this.name = name;
        this.diagnosis = diagnosis;
        this.mobilityStatus = mobilityStatus;
        this.startSleepTime = startSleepTime;
        this.endSleepTime = endSleepTime;
        this.address = address;
        this.avgSleepTime = avgSleepTime;
    }

}

