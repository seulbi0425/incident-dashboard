package com.fnf.incident.domain.auth;

import com.fnf.incident.domain.auth.dto.LoginRequest;
import com.fnf.incident.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자/인증", description = "계정 생성 및 로그인 API")
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // 암호화 도구 가져오기

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1) 사용자 만들기 - 비밀번호를 암호화해서 저장
    @Operation(summary = "계정 생성", description = "새 사용자 계정을 생성합니다. 비밀번호는 암호화하여 저장됩니다.")
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        // 입력받은 비밀번호를 암호화한 값으로 바꿔서 저장
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // 2) 로그인 - 암호화된 비밀번호끼리 비교
    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인합니다. 암호화된 비밀번호를 비교해 성공/실패 메시지를 반환합니다.")
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest input) {
        User found = userRepository.findByLoginId(input.getLoginId());

        if (found == null) {
            return "실패: 존재하지 않는 아이디입니다";
        }
        // 입력한 비밀번호와 DB의 암호화된 비밀번호를 비교 (matches가 알아서 비교해줌)
        if (!passwordEncoder.matches(input.getPassword(), found.getPassword())) {
            return "실패: 비밀번호가 일치하지 않습니다";
        }
        return "성공: " + found.getName() + "님 환영합니다 (권한: " + found.getRole() + ")";
    }
}