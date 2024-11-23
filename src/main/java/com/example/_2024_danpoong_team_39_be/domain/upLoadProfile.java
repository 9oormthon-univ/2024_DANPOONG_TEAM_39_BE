package com.example._2024_danpoong_team_39_be.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class upLoadProfile {
    @Id
    private Long Id;

    private String uploadProfileName; // 업로드된 파일명
    private String storeProfileName; // 저장된 파일명

    public upLoadProfile(String uploadProfileName, String storeProfileName) {
        this.uploadProfileName = uploadProfileName;
        this.storeProfileName = storeProfileName;
    }

}