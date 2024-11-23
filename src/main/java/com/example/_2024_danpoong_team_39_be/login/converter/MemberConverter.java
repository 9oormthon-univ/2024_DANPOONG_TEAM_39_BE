package com.example._2024_danpoong_team_39_be.login.converter;


import com.example._2024_danpoong_team_39_be.domain.Member;
import com.example._2024_danpoong_team_39_be.login.dto.MemberRequestDTO;
import com.example._2024_danpoong_team_39_be.login.dto.MemberResponseDTO;
import lombok.Builder;


public class MemberConverter {

    // Member 객체를 UserResponseDTO.JoinResultDTO로 변환하는 메서드
    public static MemberResponseDTO.JoinResultDTO toJoinResultDTO(Member member) {
        if (member == null) {
            return null;
        }

        return MemberResponseDTO.JoinResultDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .alias(member.getAlias())
                .build();
    }

}

