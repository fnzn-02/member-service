package com.example.member_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // 자동 시간 기록 작동
@SpringBootApplication
public class MemberServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MemberServiceApplication.class, args);
	}
}
