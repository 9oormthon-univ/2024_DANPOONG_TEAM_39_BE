package com.example._2024_danpoong_team_39_be.login.controller;



import com.example._2024_danpoong_team_39_be.login.dto.FillupResultDTO;
import com.example._2024_danpoong_team_39_be.login.service.AuthService;
import com.example._2024_danpoong_team_39_be.login.BaseResponse;
import com.example._2024_danpoong_team_39_be.login.converter.MemberConverter;
import com.example._2024_danpoong_team_39_be.domain.Member;
import com.example._2024_danpoong_team_39_be.login.dto.MemberRequestDTO;
import com.example._2024_danpoong_team_39_be.login.dto.MemberResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AuthController {

    private final AuthService authService;
//    private final MemberService memberService;

//    // 회원가입용 폼 추가 입력 url
//    @PostMapping("/api/member/signup/fillup")
//    public BaseResponse<MemberRequestDTO.FillupRequestDTO> fillUpMember(
//            @RequestBody MemberRequestDTO.FillupRequestDTO fillupRequestDTO) {
//        Member updatedMember = memberService.updateMember(fillupRequestDTO);
//        MemberRequestDTO.FillupRequestDTO responseDTO = MemberConverter.toMember(updatedMember);
//        return BaseResponse.onSuccess(updatedMember); // 추후 jwt 로직 추가 예정(토큰으로 프론트에 보내줌)
//    }

    // 멤버 추갖 ㅓㅇ보 입력
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
    public BaseResponse<MemberResponseDTO.JoinResultDTO> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse) {
        Member member = authService.oAuthLogin(accessCode, httpServletResponse);
        return BaseResponse.onSuccess(MemberConverter.toJoinResultDTO(member));
    }
}
