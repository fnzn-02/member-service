package com.example.member_service.config;

import com.example.member_service.jwt.JwtFilter;
import com.example.member_service.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration // 스프링 설정 파일
@EnableWebSecurity // 스프링 시큐리티(경호원) 기능 활성화
@RequiredArgsConstructor // JwtUtil 기계를 주입받기 위해 추가
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors-> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/members/signup",// 회원가입
                                "/api/members/login", // 로그인
                                "/api/members/password/code", // 비번 찾기 코드 발송
                                "/api/members/password/reset", // 비번 재설정
                                "/api/members/signup/code", // 이메일 코드 발송
                                "/api/members/signup/verify", // 이메일 인증 확인 API
                                "/error").permitAll() // 회원가입 경로는 아무나 들어오게 프리패스
                        .anyRequest().authenticated() // 그 외에 다른 곳은 전부 다 로그인 검사
                )
                // 우리가 만든 문지기(JwtFilter)를 스프링의 기본 문지기보다 맨 앞에 떡하니 세우기
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // 앞으로 비밀번호는 BCrypt로 갈아버림
    }

    // 프론트엔드 접근 허용(CORS) 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 프론트엔드랑 안전하게 통신(인증정보 공유) 허용
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173")); // 허용할 프론트엔드 주소 (리액트 3000, 비트 5173 등 로컬 테스트용)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // 허용할 HTTP 방식
        config.setAllowedHeaders(List.of("*")); // 모든 헤더(데이터 종류) 허용
        config.setExposedHeaders(List.of("Authorization")); // 프론트엔드가 응답해서 JWT 토큰을 꺼내갈 수 있게 노출

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 우리 서버의 모든 API 주소("/**")에 이 룰을 적용
        return source;
    }
}
