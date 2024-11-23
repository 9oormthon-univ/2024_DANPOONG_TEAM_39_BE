package com.example._2024_danpoong_team_39_be.welfare;


import com.example._2024_danpoong_team_39_be.login.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class WelfareController {

    @PostMapping("/api/welfare")
    public BaseResponse<WelfareDTO.WelfareResponseDTO> welfareLookUp(
            @RequestBody WelfareDTO.WelfareRequestDTO welfareSelect) {

        // Service 호출 및 결과 받기
        WelfareDTO.WelfareResponseDTO response = WelfareService.welfareLookUp(welfareSelect);

        // 성공 응답 반환
        return BaseResponse.onSuccess(response);
    }
}
