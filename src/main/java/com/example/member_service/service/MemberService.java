package com.example.member_service.service;

import com.example.member_service.dto.*;
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
    private final EmailService emailService;

    @Transactional
    public void signup(SignupRequestDto requestDto){

        // 이메일 중복 검사
        if(memberRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 10분 이내에 인증을 마친 상태인지 확인
        if(!emailService.isVerified(requestDto.getEmail())){
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았거나 10분 유효시간이 만료되었습니다.");
        }

        // 닉네임 중복 검사
        if(memberRepository.findByNickname(requestDto.getNickname()).isPresent()){
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 포장 (빌더 패턴 적용)
        Member newMember = Member.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
                .build();

        // 창고에 저장
        memberRepository.save(newMember);

        // 회원가입이 끝나면 인증 상태를 완전히 파기
        emailService.clearVerification(requestDto.getEmail());
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

    public void sendSignupCode(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        emailService.sendVerificationCode(email);
    }

    // 내 정보 조회 (마이페이지)
    @Transactional(readOnly = true) // 데이터 변경 없이 '읽기'만 할 때 붙여주면 성능이 좋아짐
    public MemberResponseDto getMyInfo(String email){
        // DB에서 이메일로 유저 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 찾은 유저 정보를 상자에 담아서 반환
        return new MemberResponseDto(member);
    }

    // 닉네임 수정 로직
    @Transactional
    public void updateNickname(String email, NicknameUpdateRequestDto requestDto){
        // DB에서 이메일로 유저 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 새 닉네임이 이미 존재하는지 검사
        if(memberRepository.findByNickname(requestDto.getNewNickname()).isPresent()){
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 엔터티에 만들어둔 스위치를 눌러서 닉네임 변경
        member.updateNickname(requestDto.getNewNickname());
    }

    // 회원 탈퇴 로직
    @Transactional
    public void deleteMember(String email){
        // DB에서 이메일로 유저 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다,"));

        // DB에서 해당 유저 완전히 삭제
        memberRepository.delete(member);
    }

    // 비밀번호 변경 로직
    @Transactional
    public void updatePassword(String email, PasswordUpdateRequestDto requestDto){
        // DB에서 이메일로 유저 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 본인 확인: 지금 입력한 현재 비밀번호가 DB에 있는 암호화된 비밀번호랑 맞는지 비교
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), member.getPassword())){
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 검문 통과. 새 비밀번호를 암호화 기계에 넣고 돌리기
        String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());

        // 암호화된 새 비밀번호로 스위치 눌러서 DB 업데이트
        member.updatePassword(encodedNewPassword);
    }

    // 인증번호 확인 후 비밀번호 변경
    @Transactional
    public void resetPassword(String email, String code, String newPassword){

        // 유저가 보낸 인증번호가 맞는지 우체부한테 물어보기
        if(!emailService.verifyCode(email, code)){
            throw new IllegalArgumentException("인증번호가 틀렸거나 만료되었습니다.");
        }

        // 이메일로 유저 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 새 비밀번호 암호화해서 DB에 저장
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedPassword);
    }
}
