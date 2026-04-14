package com.example.member_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 상자에 값을 한 번에 넣을 수 있게 해줌
public class LoginResponseDto {
    private String accessToken; // 토큰이 들어갈 자리
}
