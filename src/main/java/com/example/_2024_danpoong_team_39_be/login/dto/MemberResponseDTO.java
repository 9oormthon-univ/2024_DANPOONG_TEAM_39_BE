package com.example._2024_danpoong_team_39_be.login.dto;


import com.example._2024_danpoong_team_39_be.domain.Gender;
import com.example._2024_danpoong_team_39_be.domain.upLoadProfile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.example._2024_danpoong_team_39_be.domain.Member;
@Getter
@Setter
public class MemberResponseDTO {



    @Builder
    @Getter
    @Setter
    public static class JoinResultDTO {
        private Long id;
        private String email;
        private String alias;

        // 기본 생성자 추가
        public JoinResultDTO() {
        }

        // 전체 매개변수 생성자 (빌더 사용 시 자동 생성)
        @Builder
        public JoinResultDTO(Long id, String email, String alias) {
            this.id = id;
            this.email = email;
            this.alias = alias;
        }

        public static JoinResultDTO fromMember(Member member) {
            return JoinResultDTO.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .alias(member.getAlias())
                    .build();
        }
    }


}


