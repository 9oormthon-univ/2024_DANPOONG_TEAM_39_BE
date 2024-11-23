package com.example._2024_danpoong_team_39_be.login.service;


import com.example._2024_danpoong_team_39_be.domain.Member;
import com.example._2024_danpoong_team_39_be.login.converter.AuthConverter;
import com.example._2024_danpoong_team_39_be.login.dto.KakaoDTO;
import com.example._2024_danpoong_team_39_be.login.repository.MemberRepository;
import com.example._2024_danpoong_team_39_be.login.util.JwtUtil;
import com.example._2024_danpoong_team_39_be.login.util.KakaoUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.example._2024_danpoong_team_39_be.login.dto.MemberRequestDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoUtil kakaoUtil;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    //@Override
    public Member oAuthLogin(String accessCode, HttpServletResponse httpServletResponse) {

        //카카오 서버에 토큰 요청
        log.info("카카오 토큰 요청 시작");
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        log.info("카카오 토큰 요청 끝");

        // 토큰으로 카카오 유저 정보 받아옴
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
        log.info("Kakao profile received: " + kakaoProfile.toString());

        // member 식별자로 이메일 사용 -> 이메일 중복 여부로 로그인과 회원가입 판단
        String email = kakaoProfile.getKakao_account().getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> createNewmemberByKakao(kakaoProfile));

        // header에 넣어 사용자 로그인용 토큰 생성
        String token = jwtUtil.createAccessToken(member.getEmail());
        httpServletResponse.setHeader("Authorization", token);
        log.info("사용자 로그인용 토큰:"+token);

        return member;

    }

    // 새 member 생성 후 DB에 저장 (email이 식별자)
    // 추후 member의 다른 값들 사용자 입력을 통해 받아야 함
    private Member createNewmemberByKakao(KakaoDTO.KakaoProfile kakaoProfile) {
        Member newMember = AuthConverter.toMember(
                kakaoProfile.getKakao_account().getEmail(),
                kakaoProfile.getKakao_account().getProfile().getNickname()

        );

        return memberRepository.save(newMember);
    }

    // 회원 추가정보 입력
    public Member updateAdditionalInfo(String token, MemberRequestDTO.UpdateInfoRequestDTO updateInfo) {
        // 토큰에서 이메일 추출
        String email = jwtUtil.getEmailFromToken(token);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email +"해당 이메일을 사용하는 유저를 찾을 수 없습니다."));

        // 사용자 정보 업데이트
        if (updateInfo.getAlias() != null) member.setAlias(updateInfo.getAlias());
        if (updateInfo.getAge() > 0) member.setAge(updateInfo.getAge());
        if (updateInfo.getGender() != null) member.setGender(updateInfo.getGender());

//        이미지 저장 (개발 보류)
     //   if (updateInfo.getProfileImage() != null) member.setProfileImage(updateInfo.getProfileImage());

        // 저장
        return memberRepository.save(member);
    }

}

