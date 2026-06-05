package com.fnf.incident;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // 로그인 아이디로 사용자 찾기  ← 이 한 줄이 새로 추가된 부분
    User findByLoginId(String loginId);
}