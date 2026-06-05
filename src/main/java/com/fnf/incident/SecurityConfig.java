package com.fnf.incident;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 비밀번호 암호화 도구 (이걸 컨트롤러에서 가져다 씀)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 모든 요청을 일단 허용 (학습용 - 자동 잠금 해제)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
    // ※ 실무에선 여기서 권한별로 막지만, 지금은 학습용으로 전부 허용
}