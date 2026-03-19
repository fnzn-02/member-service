package com.example.member_service.repository;

import com.example.member_service.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// DB에 접근하여 Member 데이터를 관리하는 인터페이스
// 상속으로 save(), findById(), delete()등의 기본 CRUD 메서드를 자동으로 사용가능
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일로 회원 찾기
    Optional<Member> findByEmail(String email);

    // 닉네임 중복인지 찾기
    Optional<Member> findByNickname(String nickname);
}
