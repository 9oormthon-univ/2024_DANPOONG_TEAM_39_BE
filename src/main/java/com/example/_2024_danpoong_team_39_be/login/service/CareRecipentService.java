package com.example._2024_danpoong_team_39_be.login.service;

import com.example._2024_danpoong_team_39_be.careAssignment.CareAssignmentRepository;
import com.example._2024_danpoong_team_39_be.domain.CareAssignment;
import com.example._2024_danpoong_team_39_be.domain.CareRecipient;
import com.example._2024_danpoong_team_39_be.domain.Member;
import com.example._2024_danpoong_team_39_be.login.converter.CareRecipientConverter;
import com.example._2024_danpoong_team_39_be.login.dto.CareRecipientDTO;
import com.example._2024_danpoong_team_39_be.login.repository.CareRecipientRepository;
import com.example._2024_danpoong_team_39_be.login.repository.MemberRepository;
import com.example._2024_danpoong_team_39_be.login.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CareRecipentService {

    private final CareRecipientRepository careRecipientRepository;
    private final CareAssignmentRepository careAssignmentRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

//
//    private CareRecipient createCareRecipient(String token, CareRecipientDTO.CareRecipientProfile careRecipientProfile) {
//        String email = jwtUtil.getEmailFromToken(token);
//
//        // Member 검색
//        Member member = memberRepository.findByEmail(email) // 로그인할 때 새롭게 발급받은 jwt 토큰 사용
//                .orElseThrow(() -> new IllegalArgumentException(email + " 해당 이메일을 사용하는 유저를 찾을 수 없습니다."));
//
//        // CareRecipient 생성
//        CareRecipient newCareRecipient = CareRecipientConverter.toCareRecipient(
//                careRecipientProfile.getCareRecipientid(),
//                careRecipientProfile.getName(),
//                careRecipientProfile.getDiagnosis(),
//                careRecipientProfile.getMobilty_status(),
//                careRecipientProfile.getStart_sleep_time(),
//                careRecipientProfile.getEnd_sleep_time(),
//                careRecipientProfile.getAddress(),
//                careRecipientProfile.getAvg_sleep_time()
//        );
//
//        CareRecipient careRecipient = CareRecipient.create(newCareRecipient, Collections.singletonList(member));
//
//        member.setCareRecipient(careRecipient);
//
//        // 모든 데이터 저장
//        careRecipientRepository.save(newCareRecipient);
//
//        return careRecipientRepository.save(newCareRecipient);
//    }

    @Transactional
    public CareRecipient createCareRecipient(String token, CareRecipientDTO.CareRecipientProfile careRecipientProfile, String relationship) {
        // 토큰에서 이메일 추출
        String email = jwtUtil.getEmailFromToken(token);

        // Member 검색
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 사용하는 유저를 찾을 수 없습니다: " + email));

        // CareRecipient 생성
        CareRecipient careRecipient = CareRecipientConverter.toCareRecipient(careRecipientProfile);

        // CareAssignment 생성
        CareAssignment careAssignment = CareAssignment.create(member, careRecipient, relationship);
        careAssignment.setMember(member);
        careAssignment.setRecipient(careRecipient);
        careAssignment.setRelationship(relationship);

        // CareRecipient와 CareAssignment 저장
        careRecipient.setCareAssignment(Collections.singletonList(careAssignment)); // CareRecipient에 CareAssignment 설정
        careRecipientRepository.save(careRecipient); // CareRecipient 저장
        careAssignmentRepository.save(careAssignment); // CareAssignment 저장

        log.info("CareRecipient 및 CareAssignment 저장 완료: {}, {}", careRecipient, careAssignment);
        return careRecipient;
    }

    // CareRecipient 조회 (GET 요청)
    public CareRecipient getCareRecipient(Long id) {
        return careRecipientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CareRecipient not found with id " + id));
    }
}
