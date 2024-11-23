package com.example._2024_danpoong_team_39_be.login.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 키 생성 -> 실제 환경에서 변경 필요
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 토큰 만료 시간 (1시간 설정)
    private final long expirationTime = 1000 * 60 * 60;

    // JWT 토큰 생성
    public String createAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    // 이메일 추출
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey) // secretKey 그대로 사용
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // 이하: 대략적으로 만들어 두었고 나중에 사용할 가능성이 높아 그대로 두었음
    // JWT 토큰에서 클레임 추출
//    public Claims extractClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }

    // JWT 토큰의 유효성 검사
//    public boolean isTokenValid(String token, String username) {
//        String extractedUsername = extractClaims(token).getSubject();
//        return (extractedUsername.equals(username) && !isTokenExpired(token));
//    }

    // 토큰 만료 여부 확인
//    private boolean isTokenExpired(String token) {
//        Date expiration = extractClaims(token).getExpiration();
//        return expiration.before(new Date());
//    }
}

