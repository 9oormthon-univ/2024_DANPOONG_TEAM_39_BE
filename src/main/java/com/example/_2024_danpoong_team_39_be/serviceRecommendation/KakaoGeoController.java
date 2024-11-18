package com.example._2024_danpoong_team_39_be.serviceRecommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api")
public class KakaoGeoController {
    @Autowired
    private KakaoGeoService kakaoGeoService;
    private final Random random = new Random();
    @GetMapping("/recommendation")
    public ResponseEntity<String> searchHospital(@RequestParam String address) {
        // 주소로부터 위도/경도 변환
        String coordinates = kakaoGeoService.getCoordinatesFromAddress(address);

        if (coordinates.equals("주소에 대한 좌표를 찾을 수 없습니다.")) {
            return ResponseEntity.badRequest().body("주소를 찾을 수 없습니다.");
        }

        // 위도와 경도 분리
        String[] coord = coordinates.split(",");
        double x = Double.parseDouble(coord[1]); // 경도
        double y = Double.parseDouble(coord[0]); // 위도

        // 쿼리 배열
        String[] queries = {"데이케어 센터", "간병인", "방문요양"};
        int radius = 2000;
        // 배열 중 하나를 랜덤으로 픽
        String query = queries[random.nextInt(queries.length)];

        // 로그
        System.out.println("Received Request with query: " + query + ", x: " + x + ", y: " + y + ", radius: " + radius);

        // 장소 검색
        String result = kakaoGeoService.searchPlaces(query, x, y, radius);

        return ResponseEntity.ok(result);
    }
}
