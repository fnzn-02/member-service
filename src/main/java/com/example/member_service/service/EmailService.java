package com.example.member_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // Redis와 통신하기 위해 새로 고용한 관리자
    private final StringRedisTemplate redisTemplate;

    // 6자리 인증번호 생성 및 메일 발송
    public void sendVerificationCode(String email){
        String code = String.format("%06d", new SecureRandom().nextInt(1000000));

        // 1. Redis에 저장 (키: "EMAIL_CODE:이메일", 값: 인증번호, 수명: 10분)
        // 10분이 지나면 알아서 증발(삭제)됩니다.
        redisTemplate.opsForValue().set("EMAIL_CODE:" + email, code, 10, TimeUnit.MINUTES);

        // 기존의 인증 완료 상태가 남아있을 수 있으니 초기화
        redisTemplate.delete("VERIFIED:" + email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("OnAir <luyanfndis@gmail.com>");
        message.setSubject("[OnAir] 인증번호 발송");
        message.setText("요청하신 인증번호는 [" + code + "] 입니다. 10분 내에 정확히 입력해 주세요.");

        mailSender.send(message);
    }

    // 유저가 입력한 인증번호가 맞는지 검증
    public boolean verifyCode(String email, String inputCode){
        // Redis에서 저장된 코드 꺼내오기
        String savedCode = redisTemplate.opsForValue().get("EMAIL_CODE:" + email);

        if(savedCode != null && savedCode.equals(inputCode)){
            // 1. 인증에 성공했으니 기존 인증번호는 Redis에서 바로 파기
            redisTemplate.delete("EMAIL_CODE:" + email);

            // 2. "이 이메일은 인증을 통과했다"는 증명서를 새로 발급해서 10분간 저장
            redisTemplate.opsForValue().set("VERIFIED:" + email, "TRUE", 10, TimeUnit.MINUTES);
            return true;
        }
        return false;
    }

    // 인증완료 시점부터 10분 이내에 가입을 완료했는지 검사
    public boolean isVerified(String email){
        // Redis에 "VERIFIED:이메일" 이라는 증명서가 남아있는지만 확인하면 끝!
        return Boolean.TRUE.equals(redisTemplate.hasKey("VERIFIED:" + email));
    }

    // 가입 완료 후 보안을 위해 인증 상태 완전 삭제
    public void clearVerification(String email){
        redisTemplate.delete("VERIFIED:" + email);
    }
}