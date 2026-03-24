package com.example.member_service.dto;

import com.example.member_service.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private String email;
    private String nickname;

    // Entity를 받아서 DTO 상자로 변환해주는 생성자
    public MemberResponseDto(Member member){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
