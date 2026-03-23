package com.example.member_service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Component // 이 객체 너가 창고에 올려두고 관리하라는 뜻
public class JwtUtil {
    private final Key key;
    private final long accessTokenExpTime = 1000 * 60 * 30; // 토큰 유효기간: 30분

    // 환경변수에 숨겨둔 키를 스프링이 시핼될 때 빼오는 코드
    public JwtUtil(@Value("${jwt.secret}") String secretKey){
        // 가져온 문자열 키를 JWT 전용 암호화 열쇠로 가공하는 과정
        byte[] keyBytes = secretKey.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 진짜 토큰을 찍어내는 핵심 메서드
    public String createToken(String email){
        return Jwts.builder()
                .setSubject(email) // 이 토큰의 주인이 누구인지
                .setIssuedAt(new Date()) // 발급 시간 (지금 이 순간)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpTime)) // 만료시간 (지금부터 30분 뒤)
                .signWith(key, SignatureAlgorithm.HS256) // 서버만의 비밀 열쇠로 위조 방지 도장
                . compact(); // 이 모든 정보를 압축해서 하나의 긴 문자열(토큰)로 변환
    }
}
