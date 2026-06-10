package com.fnf.incident.domain.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")  // user는 DB 예약어라 'users'로 지정
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;   // 로그인 아이디 (예: metam_juhi)
    private String password;  // 비밀번호
    private String name;      // 이름 (예: 주휘인)
    private String role;      // 권한 (metam / logen / admin)
}