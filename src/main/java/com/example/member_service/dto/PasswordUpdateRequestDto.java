package com.example.member_service.dto;

import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {
    private String currentPassword; // 현재 비밀번호 (본인 확인용)
    private String newPassword; // 바꿀 새 비밀번호
}
