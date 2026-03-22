package com.example.member_service.service;

import com.example.member_service.dto.SignupRequestDto;
import com.example.member_service.entity.Member;
import com.example.member_service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // 상수 붙은 필수 직원들 책상(연결) 알아서 세팅해줘(롬복)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto requestDto){
        // 이메일 중복 검사
        if(memberRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 포장 (빌더 패턴 적용)
        Member newMember = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .build();

        // 창고에 저장
        memberRepository.save(newMember);
    }
}
