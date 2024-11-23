package com.example._2024_danpoong_team_39_be.login.repository;

import com.example._2024_danpoong_team_39_be.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface profileRepository extends JpaRepository<Member, Long> {

}
