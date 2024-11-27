package com.example._2024_danpoong_team_39_be.login.repository;

import com.example._2024_danpoong_team_39_be.domain.CareRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareRecipientRepository extends JpaRepository<CareRecipient, Long> {
    List<CareRecipient> id(Long id);
}

/*

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //이메일을 기준으로 사용자 검색
    Optional<Member> findByEmail(String email);
}
 */