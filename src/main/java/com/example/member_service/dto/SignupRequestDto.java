package com.example.member_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter // Service(매니저)가 private으로 잠긴 데이터를 꺼내 읽을 수 있도록 열쇠를 자동 생성
public class SignupRequestDto {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>]).{8,}$",
             message = "비밀번호는 8자 이상이어야 하며, 특수문자를 최소 1개 포함해야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;
}
