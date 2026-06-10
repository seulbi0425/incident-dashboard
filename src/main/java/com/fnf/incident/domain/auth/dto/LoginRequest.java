package com.fnf.incident.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

// 로그인 요청 전용 데이터 전송 객체
// (User 엔티티 대신 로그인에 필요한 값만 받는다)
@Getter
@Setter
public class LoginRequest {

    private String loginId;   // 로그인 아이디
    private String password;  // 비밀번호
}
