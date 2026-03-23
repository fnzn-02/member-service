package com.example.member_service.controller;

import com.example.member_service.dto.LoginRequestDto;
import com.example.member_service.dto.LoginResponseDto;
import com.example.member_service.dto.SignupRequestDto;
import com.example.member_service.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    // 안내데스크 뒤에서 대기하고 있는 실무 매니저 클래스 이름 선언
    private final MemberService memberService;

    // 손님이 가입 신청서(DTO)를 들고 찾아왔을 때 실행되는 곳
    @PostMapping("/signup") // 데이터를 저장/생성 해달라는 요청이 오면 여기로 안내
    public String signup(@RequestBody SignupRequestDto requestDto){
        // 매니저에게 신청서에 적힌 내용(이메일, 비번, 닉네임)을 넘기면서 가입시켜 달라는 명령
        memberService.signup(requestDto);
        return "회원가입이 성공적으로 완료되었습니다.";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto){
        // 손님한테 받은 Dto를 매니저(Service)한테 넘겨서 검사
        return ResponseEntity.ok(memberService.login(requestDto));
    }
}
