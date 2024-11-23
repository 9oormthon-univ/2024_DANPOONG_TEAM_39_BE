package com.example._2024_danpoong_team_39_be.login.dto;


import com.example._2024_danpoong_team_39_be.domain.Gender;
import com.example._2024_danpoong_team_39_be.domain.upLoadProfile;
import lombok.*;

@Getter
@Setter
@Builder
public class MemberRequestDTO {

    @Setter
    @Builder
    @Getter
    public static class FillupRequestDTO {

        private Long id;
        private String name;
        private String alias;
        private int age;
        private Gender gender;
        private String email;
//        private upLoadProfile profileImage;

        public FillupRequestDTO() {
        }

        public FillupRequestDTO(
                Long id, String name, String alias, int age, Gender gender, String email) {
            this.id = id;
            this.name = name;
            this.alias = alias;
            this.age = age;
            this.gender = gender;
            this.email = email;
//            this.profileImage = profileImage;
        }


    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateInfoRequestDTO {
        private Long id;
        private String name;
        private String alias;
        private int age;
        private Gender gender;
//        private String profileImage;
    }


}

