package com.example.member_service.service;

import com.example.member_service.dto.LoginRequestDto;
import com.example.member_service.dto.LoginResponseDto;
import com.example.member_service.dto.SignupRequestDto;
import com.example.member_service.entity.Member;
import com.example.member_service.jwt.JwtUtil;
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
    private final JwtUtil jwtUtil;

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

    public LoginResponseDto login(LoginRequestDto requestDto){
        // 창고에서 이메일로 유저 찾기
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 비밀번호 일치하는지 검사
        if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 검사 통과하면 기계한테 이 이메일로 토큰 뽑아달라고 명령
        String token = jwtUtil.createToken(member.getEmail());

        // 뽑아낸 토큰을 손님에게 전달
        return new LoginResponseDto(token);
    }
}
