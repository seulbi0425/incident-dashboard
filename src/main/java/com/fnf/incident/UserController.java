package com.fnf.incident;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1) 테스트용 사용자 1명 만들기 (로그인 시험하려면 사용자가 있어야 함)
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // 2) 로그인
    @PostMapping("/login")
    public String login(@RequestBody User input) {
        // 입력한 아이디로 사용자 찾기
        User found = userRepository.findByLoginId(input.getLoginId());

        // 그런 아이디가 없으면
        if (found == null) {
            return "실패: 존재하지 않는 아이디입니다";
        }
        // 비밀번호가 다르면
        if (!found.getPassword().equals(input.getPassword())) {
            return "실패: 비밀번호가 일치하지 않습니다";
        }
        // 둘 다 통과하면 성공
        return "성공: " + found.getName() + "님 환영합니다 (권한: " + found.getRole() + ")";
    }
}