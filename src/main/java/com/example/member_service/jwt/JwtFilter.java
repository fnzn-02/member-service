package com.example.member_service.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// OncePerRequestFilter: 손님이 요청을 보낼 때마다 딱 한 번씩만 검사하는 역할
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // 우리가 만든 스캐너(기계) 쥐여주기

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

        // 손님이 가져온 봉투(HTTP Header)에서 Authorization 이라는 이름이 적힌 곳을 열어봄
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        // 토큰은 보통 Bearer 라는 단어로 시작함 (통행증이라는 뜻)
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            // Bearer (공백 포함 7글자)를 잘라내고, 뒤에 있는 토큰만 빼옴
            token = authorizationHeader.substring(7);
        }

        // 토큰을 가져왔고, 우리 스캐너(validateToken)로 검사했을 때 위조되지 않은 진짜 라면
        if(token != null && jwtUtil.validateToken(token)){
            // 토큰 알맹이를 까서 이메일 꺼내오기
            String email = jwtUtil.getEmailFromToken(token);

            // 이 손님(이메일)은 검문 통과한 확실한 유저입니다 하고 서버 전체에 신원 보증서 등록
            // 뒤에 null 두 개는 당장 안 쓰는 비밀번호나 권한 정보 자리
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 검사가 끝났으니 다음 문지기한테 넘기거나, 최종 목적지(Controller)로 손님 들여보내기
        filterChain.doFilter(request, response);
    }
}
