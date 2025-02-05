package com.example._2024_danpoong_team_39_be.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

// Member domain

@Entity
@Setter @Getter @ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String password;
    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String alias;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length = 50)
    private String email;
    @Column
    private String role;
    @JsonBackReference  // 이 어노테이션으로 순환참조를 방지합니다.
    @OneToOne(mappedBy = "member")
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
