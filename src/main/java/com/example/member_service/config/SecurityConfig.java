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

@Configuration // 스프링 설정 파일
@EnableWebSecurity // 스프링 시큐리티(경호원) 기능 활성화
@RequiredArgsConstructor // JwtUtil 기계를 주입받기 위해 추가
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/members/signup",
                                "/api/members/login",
                                "/api/members/password/code",
                                "/api/members/password/reset",
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
}
