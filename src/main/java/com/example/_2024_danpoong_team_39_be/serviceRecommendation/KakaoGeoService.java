package com.example._2024_danpoong_team_39_be.serviceRecommendation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoGeoService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${kakao.api.key}")
    private String apiKey;

    // 주소로부터 위도/경도를 얻는 메서드
    public String getCoordinatesFromAddress(String address) {
        String url = "https://dapi.kakao.com/v2/local/search/address.json";

        // Request headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // URL과 파라미터 구성
        String finalUrl = String.format("%s?query=%s", url, address);

        // 카카오 API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        // 응답에서 위도/경도 추출
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // "documents" 배열에서 첫 번째 항목 가져오기
            JsonNode documentsNode = rootNode.path("documents");
            if (documentsNode.isArray() && documentsNode.size() > 0) {
                JsonNode firstPlace = documentsNode.get(0);
                double latitude = firstPlace.path("y").asDouble();
                double longitude = firstPlace.path("x").asDouble();
                return String.format("%f,%f", latitude, longitude); // 위도, 경도를 문자열로 반환
            } else {
                return "주소에 대한 좌표를 찾을 수 없습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing the response.";
        }
    }

    // 장소 검색 메서드
    public String searchPlaces(String query, double x, double y, int radius) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";

        // Request headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // URL과 파라미터 구성
        String encodedQuery = query;
        String finalUrl = String.format("%s?query=%s&x=%f&y=%f&radius=%d", url, encodedQuery, x, y, radius);

        // 카카오 API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        // 응답에서 가장 가까운 장소를 추출
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // "documents" 배열에서 첫 번째 항목 가져오기
            JsonNode documentsNode = rootNode.path("documents");
            if (documentsNode.isArray() && documentsNode.size() > 0) {
                JsonNode firstPlace = documentsNode.get(0);
                return firstPlace.toString(); // 첫 번째 항목을 문자열로 반환
            } else {
                return "키워드 관련 장소를 찾지 못했습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing the response.";
        }
    }
}
