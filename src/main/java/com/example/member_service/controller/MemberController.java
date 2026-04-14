package com.example.member_service.controller;

import com.example.member_service.dto.*;
import com.example.member_service.service.EmailService;
import com.example.member_service.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    // 안내데스크 뒤에서 대기하고 있는 실무 매니저 클래스 이름 선언
    private final MemberService memberService;
    private final EmailService emailService;

    // 회원가입 API
    // 손님이 가입 신청서(DTO)를 들고 찾아왔을 때 실행되는 곳
    @PostMapping("/signup") // 데이터를 저장/생성 해달라는 요청이 오면 여기로 안내
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto){
        // 매니저에게 신청서에 적힌 내용(이메일, 비번, 닉네임)을 넘기면서 가입시켜 달라는 명령
        memberService.signup(requestDto);
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }

    // 회원가입 전용 인증번호 발송
    @PostMapping("/signup/code")
    public ResponseEntity<String> sendSignupCode(@RequestParam("email") String email){
        memberService.sendSignupCode(email);
        return ResponseEntity.ok("인증번호가 발송되었습니다.");
    }

    // 인증번호가 맞는지 화면에서 바로 확인하기 위한 API
    @PostMapping("/signup/verify")
    public ResponseEntity<String> verifyCode(@RequestParam("email") String email, @RequestParam("code") String code){
        if(emailService.verifyCode(email, code)){
            return ResponseEntity.ok("인증에 성공했습니다.");
        } else{
            return ResponseEntity.badRequest().body("인증번호가 틀렸거나 만료되었습니다.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto){
        // 손님한테 받은 Dto를 매니저(Service)한테 넘겨서 검사
        return ResponseEntity.ok(memberService.login(requestDto));
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyInfo(Principal principal){
        // 문지기가 검문을 통과시키면서 이 사람 이메일 00입니다 라고 적어둔 신원 보증서
        // principal.getName()만 호출하면, 토큰 검사 없이 바로 본인 이메일 꺼내 쑬 수 있음
        String email = principal.getName();

        // 매니저한테 이메일 넘겨서 정보 찾아오라고 시키기
        return ResponseEntity.ok(memberService.getMyInfo(email));
    }

    // 닉네임 수정 API
    @PutMapping("/me/nickname")
    public ResponseEntity<String> updateNickname(Principal principal, @RequestBody NicknameUpdateRequestDto requestDto){
        // 문지기가 학인해준 이메일 꺼내기
        String email = principal.getName();

        // 매니저한테 이메일이랑 새 닉네임 상자 넘겨주면서 바꿔오라는 명령
        memberService.updateNickname(email, requestDto);

        return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
    }

    // 회원 탈퇴 API
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMember(Principal principal){
        // 문지기가 확인해준 이메일 꺼내기
        String email = principal.getName();

        // 매니저한테 이메일 넘겨주면서 db에서 삭제 명령
        memberService.deleteMember(email);

        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다. 이용해 주셔서 감사합니다.");
    }

    // 비밀번호 변경 API
    @PutMapping("/me/password")
    public ResponseEntity<String> updatePassword(Principal principal, @RequestBody PasswordUpdateRequestDto requestDto){
        // 문지기가 확인해준 이메일 꺼내기
        String email = principal.getName();

        // 매니저한테 이메일이랑 비밀번호 상자 넘겨주면서 검사 명령
        memberService.updatePassword(email, requestDto);

        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    // 인증번호 이메일로 쏘기 API
    @PostMapping("/password/code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam("email") String email){
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok("인증번호가 메일로 발송되었습니다.");
    }

    // 인증번호 확인 & 비밀번호 변경 API
    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request){
        String email = request.get("email");
        String code = request.get("code");
        String newPassword = request.get("newPassword");

        memberService.resetPassword(email, code, newPassword);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다. 새 비밀번호로 로그인해주세요.");
    }
}
