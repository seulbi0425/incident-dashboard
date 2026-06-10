package com.fnf.incident.domain.auth;

import com.fnf.incident.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// 서버가 시작될 때 테스트 계정 3개를 자동으로 만들어 줌
// (이미 있으면 건너뛰고, 없을 때만 암호화해서 저장)
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 만들어 둘 테스트 계정 목록 (loginId, password, name, role)
        createIfNotExists("master", "master1234", "본사관리자", "admin");
        createIfNotExists("metam", "metam1234", "메타엠담당", "metam");
        createIfNotExists("logen", "logen1234", "로젠담당", "logen");
    }

    // 같은 loginId가 없을 때만 새로 만든다 (중복 생성 방지)
    private void createIfNotExists(String loginId, String rawPassword, String name, String role) {
        if (userRepository.findByLoginId(loginId) != null) {
            return;  // 이미 있으면 아무것도 안 함
        }
        User user = new User();
        user.setLoginId(loginId);
        user.setPassword(passwordEncoder.encode(rawPassword));  // 비밀번호 암호화
        user.setName(name);
        user.setRole(role);
        userRepository.save(user);
        System.out.println("[DataInitializer] 테스트 계정 생성: " + loginId + " (" + role + ")");
    }
}
