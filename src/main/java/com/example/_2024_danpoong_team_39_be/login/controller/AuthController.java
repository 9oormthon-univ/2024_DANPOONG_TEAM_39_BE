package com.example._2024_danpoong_team_39_be.login.controller;



import com.example._2024_danpoong_team_39_be.domain.CareRecipient;
import com.example._2024_danpoong_team_39_be.login.converter.CareRecipientConverter;
import com.example._2024_danpoong_team_39_be.login.converter.MemberConverter;
import com.example._2024_danpoong_team_39_be.login.dto.CareRecipientDTO;
import com.example._2024_danpoong_team_39_be.login.dto.FillupResultDTO;
import com.example._2024_danpoong_team_39_be.login.dto.MemberResponseDTO;
import com.example._2024_danpoong_team_39_be.login.service.AuthService;
import com.example._2024_danpoong_team_39_be.login.BaseResponse;
import com.example._2024_danpoong_team_39_be.domain.Member;
import com.example._2024_danpoong_team_39_be.login.dto.MemberRequestDTO;
import com.example._2024_danpoong_team_39_be.login.service.CareRecipentService;
import com.example._2024_danpoong_team_39_be.login.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;


    // 멤버 추가 정보 입력
    @PutMapping("api/member/signup/fillup")
    public ResponseEntity<FillupResultDTO> updateAdditionalInfo(
            @RequestHeader("Authorization") String token,
            @RequestBody MemberRequestDTO.UpdateInfoRequestDTO updateInfo) {
        String jwtToken = token.replace("Bearer ", "");
        Member updatedMember = authService.updateAdditionalInfo(jwtToken, updateInfo);
        return ResponseEntity.ok(FillupResultDTO.of(updatedMember));
    }


    // 카카오 로그인 (토큰 요청 후 로그인)
    @GetMapping("/api/member/signup")
    public ResponseEntity<Void> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse) {
        authService.oAuthLogin(accessCode, httpServletResponse);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/member/info")
    public BaseResponse<MemberResponseDTO.JoinResultDTO> fillupInfo(@RequestHeader("Authorization") String token){
        Member member = authService.memberInfo(token);
        return BaseResponse.onSuccess(MemberConverter.toJoinResultDTO(member));
    }

    //코드짜려고 가져옴
//    @GetMapping("/api/member/signup")
//    public BaseResponse<MemberResponseDTO.JoinResultDTO> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse) {
//        Member member = authService.oAuthLogin(accessCode, httpServletResponse);
//        return BaseResponse.onSuccess(MemberConverter.toJoinResultDTO(member));
//    }




    @RequiredArgsConstructor
    @RestController
    public class CareRecipientController {

        private final CareRecipentService careRecipentService;

        @PostMapping("/api/care_recipient")
        public BaseResponse<CareRecipientDTO.CareRecipientProfile> CareRecipientFillup(
                @RequestHeader("Authorization") String token,
                @RequestBody CareRecipientDTO.CareRecipientProfile careRecipientProfile,
                @RequestParam(defaultValue = "unknown") String relationship) {

            // CareRecipient 생성
            CareRecipient careRecipient = careRecipentService.createCareRecipient(token, careRecipientProfile, relationship);

            log.info("CareRecipient 값:"+careRecipient);
            // DTO로 변환 후 반환
            return BaseResponse.onSuccess(CareRecipientConverter.toCareRecipient(careRecipient));
        }
    }

    //테스트용 컨트롤러(DB에서 member 생성했을 때 토큰 반환용)

}
