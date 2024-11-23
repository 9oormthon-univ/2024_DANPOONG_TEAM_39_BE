package com.example._2024_danpoong_team_39_be.login.dto;

import com.example._2024_danpoong_team_39_be.domain.Gender;
import com.example._2024_danpoong_team_39_be.domain.Member;
import com.example._2024_danpoong_team_39_be.domain.upLoadProfile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// 회원 추가정보 넣는 용도
@Getter
@Setter
public class FillupResultDTO {
    private Long id;
    private String name;
    private String alias;
    private int age;
    private Gender gender;
    private String email;
    private upLoadProfile profileImage;

    // 기본 생성자
    public FillupResultDTO() {
    }

    // 모든 필드 포함 생성자 (빌더 패턴용)
    @Builder
    public FillupResultDTO(Long id, String name, String alias, int age, Gender gender, int phoneNum, String email, upLoadProfile profileImage) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.profileImage = profileImage;
    }

    // Member 엔티티로부터 FillupResultDTO 생성
    public static FillupResultDTO fromMember(Member member) {
        return FillupResultDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .alias(member.getAlias())
                .age(member.getAge())
                .gender(member.getGender())
                .email(member.getEmail())
//                .profileImage(member.getProfileImage())
                .build();
    }
    public static FillupResultDTO of(Member updatedMember) {
        if (updatedMember == null) {
            return null;
        }
        return FillupResultDTO.fromMember(updatedMember);

    }

}