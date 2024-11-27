package com.example._2024_danpoong_team_39_be.domain;

import com.example._2024_danpoong_team_39_be.calendar.Calendar;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor  // 모든 필드를 초기화하는 생성자 추가
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "careAssignment", fetch = FetchType.LAZY)
    private List<Calendar> calendars;

    @OneToOne(mappedBy = "careAssignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Member member;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient", nullable = false)
    private CareRecipient recipient;

    @Column(length=50)
    private String relationship;

    public CareAssignment(Long id, Member member, CareRecipient recipient, String relationship) {
        this.id = id;
        this.member = member;
        this.recipient = recipient;
        this.relationship = relationship;
    }
    private String email;
    // Member의 email을 가져오는 메서드 추가
    public String getEmail() {
        return this.member != null ? this.member.getEmail() : null;
    }
}
