package com.example.member_service.dto;

import lombok.Getter;

@Getter // Service(매니저)가 private으로 잠긴 데이터를 꺼내 읽을 수 있도록 열쇠를 자동 생성
public class SignupRequestDto {
    private String email;
    private String password;
    private String nickname;
}
