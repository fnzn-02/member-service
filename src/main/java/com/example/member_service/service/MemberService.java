package com.example.member_service.service;

import com.example.member_service.entity.Member;
import com.example.member_service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // 상수 붙은 필수 직원들 책상(연결) 알아서 세팅해줘(롬복)
public class MemberService {
    private final MemberRepository memberRepository;

    public void signup(String email, String password, String nickname){
        // 이메일 중복 검사
        if(memberRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 포장 (빌더 패턴 적용)
        Member newMember = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        // 창고에 저장
        memberRepository.save(newMember);
    }
}
