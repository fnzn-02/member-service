package com.example.member_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {
    // 설정 파일에 적어둔 구글 서버 열쇠를 들고 있는 우체부 객체
    private final JavaMailSender mailSender;

    // 이메일(Key)과 6자리 인증번호를 임시로 적어둘 메모장 (실무의 Redis 역할)
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    // 6자리 인증번호 생성 및 메일 발송
    public void sendVerificationCode(String email){

        // 6자리 랜덤 숫자 생성
        String code = String.format("%06d", new Random().nextInt(1000000));

        // 메모장에 저장
        verificationCodes.put(email, code);

        // 메일 쏘기
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[Member Service] 비밀번호 재설정 인증번호");
        message.setText("요청하신 인증번호는 [" + code + "] 입니다. 정확히 입력해 주세요.");

        mailSender.send(message);
    }

    // 유저가 입력한 인증번호가 맞는지 검증
    public boolean verifyCode(String email, String inputCode){
        String savedCode = verificationCodes.get(email);

        // 메모장에 적힌 번호랑 유저가 보낸 번호가 일치한다면
        if(savedCode != null && savedCode.equals(inputCode)){
            verificationCodes.remove(email); // 인증 성공했으니 메모장에서 삭제
            return true;
        }
        return false;
    }
}
