package com.example.member_service.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // 생성일/수정일 자동 기록을 위해 감시자 부착
public class Member {

    @Id // 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 생성을 DB에 위임
    private Long id;

    // 이메일,
    @Column(nullable = false, unique = true) // 필수 입력값(Not Null) + 이메일 중복 가입 방지(Unique)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // Enum(USER, ADMIN)을 DB에 숫자가 아닌 알아보기 쉬운 문자열로 저장
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, unique = true)
    private String nickname;

    @CreatedDate // 데이터가 처음 Insert 될 때 현재 시간 자동 기록
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @LastModifiedDate // 데이터가 Update 될 때마다 현재 시간 자동으로 갱신
    private LocalDateTime updatedAt;

    @Builder
    public Member(String email, String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
