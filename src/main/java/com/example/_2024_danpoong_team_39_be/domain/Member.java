package com.example._2024_danpoong_team_39_be.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// Member domain
@Setter
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String alias;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length = 50)
    private String email;

    @OneToOne
    @JoinColumn(name = "care_assignment_id") // CareAssignment에 속함
    private CareAssignment careAssignment;

//    이미지 받기 구현 생략
//    @OneToOne(cascade = CascadeType.ALL)
//    private upLoadProfile profileImage;

    public Member(Long id, String name, String alias, int age, Gender gender, String email, CareAssignment careAssignment) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.careAssignment = careAssignment;

    }



}
