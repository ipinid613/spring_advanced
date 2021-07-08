package com.sparta.w1homework.controller;



import com.sparta.w1homework.dto.SignupRequestDto;
import com.sparta.w1homework.model.User;
import com.sparta.w1homework.model.UserRole;
import com.sparta.w1homework.repository.UserRepository;
import com.sparta.w1homework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;

@Controller
public class UserController {

    //UserService를 Bean으로 주입받는 부분 추가. 13~19
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원 로그인 페이지
    @GetMapping("/user/login")
    public String login() {
        return "login";
    } // 로그인 요청이 오면 "login"패이지로 이동함. 이게 어떻게 가능하냐면 타임리프 때문인데, "login"입력해주면, 타임리프의 루트인
    // resources/static에서 "login.html"을 찾아서 내려줌!!

    //로그인 에러 페이지
    @GetMapping("/user/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login"; // 로그인 에러 시에도 login.html을 내려주지만, 에러에 대한 표시가 되도록 설정.
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    public String signup() {
        return "signup";
    }

    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public String registerUser(SignupRequestDto requestDto, Model model) {
        try {
            userService.registerUser(requestDto);
        }catch (IllegalArgumentException e){
            System.out.println(e);
            model.addAttribute("message", e.getMessage());
            return "signup";
        }
        return "redirect:/user/login";
    }

    //인가받지 않은 사용자가 허용되지 않은 페이지 접근 시 아래를 리턴
    @GetMapping("/user/forbidden")
    public String forbidden() {
        return "forbidden";
    }

    //카카오로부터 오는 콜백을 처리하는 부분
    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(String code) {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드. == String code의 내용
        userService.kakaoLogin(code);

        return "redirect:/";
    }
    //테스트를 위한 코드
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/test/create")
    @ResponseBody
    public User test() {
        // 회원 "user1" 객체 추가
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setEmail("user1@sprata.com");
        user1.setRole(UserRole.USER);
        // 회원 "user1" 객체를 영속화
        userRepository.save(user1); // user1: User@12268

        // 회원 "user1" 을 조회 user1: User@12268
        User foundUser1 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 을 또 조회 user1: User@12268
        User foundUser2 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 을 또또 조회 user1: User@12268
        User foundUser3 = userRepository.findByUsername("user1").orElse(null);

        System.out.println("foundUser1:" + foundUser1 + " ,foundUser2: " + foundUser2 + ", foundUser3:" + foundUser3);
        ///// user1과 foundUser1~3은 동일성이 있음. 영속성 컨텍스트 때문임. == UserRepositoryTest 확인하면 이해하기 쉬울거다!
        // 테스트 회원 데이터 삭제
        userRepository.delete(user1);
        return user1;
    }

    @GetMapping("/user/test/delete")
    @ResponseBody
    public User deleteUser() {
        // 테스트 회원 "user1" 생성
        createTestUser1();

        // 회원 "user1" 조회
        User foundUser1 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 삭제
        userRepository.deleteById(foundUser1.getId());

        // 회원 "user1" 조회
        User deletedUser1 = userRepository.findByUsername("user1").orElse(null);

        // -------------------
        // 테스트 회원 "user1" 생성
        createTestUser1();

        // 회원 "user1" 또 조회
        User againUser2 = userRepository.findByUsername("user1").orElse(null);
        System.out.println(againUser2);

        // 테스트 회원 데이터 삭제
        userRepository.delete(againUser2);
        return againUser2;
    }

    @GetMapping("/user/test/update/1")
    @ResponseBody
    public User updateUser1() {
        // 테스트 회원 "user1" 생성
        createTestUser1();

        // 회원 "user1" 객체 추가
        // 회원 "user1" 을 조회
        User user1 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 이 존재하면,
        if (user1 != null) {
            // 회원의 email 변경
            user1.setEmail("updateUser1@sparta.com");
            // 회원의 role 변경 (USER -> ADMIN)
            user1.setRole(UserRole.ADMIN);
        }

        // 회원 "user1" 을 또 조회
        User user2 = userRepository.findByUsername("user1").orElse(null);

        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user1.getId());
        System.out.println(user2.getId());
        System.out.println(user1.getEmail());
        System.out.println(user2.getEmail());
        System.out.println(user1.getRole());
        System.out.println(user2.getRole());

        // 테스트 회원 데이터 삭제
        userRepository.delete(user2);

        return user2;
    }

    @GetMapping("/user/test/update/2")
    @ResponseBody
    public User updateUser2() {
        // 테스트 회원 "user1" 생성
        createTestUser1();

        // 회원 "user1" 객체 추가
        // 회원 "user1" 을 조회
        User user1 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 이 존재하면,
        if (user1 != null) {
            // 회원의 email 변경
            user1.setEmail("updateUser1@sparta.com");
            // 회원의 role 변경 (USER -> ADMIN)
            user1.setRole(UserRole.ADMIN);
        }

        // user1 을 저장
        userRepository.save(user1);

        System.out.println(user1);
        System.out.println(user1.getId());
        System.out.println(user1.getEmail());
        System.out.println(user1.getRole());

        // 테스트 회원 데이터 삭제
        userRepository.delete(user1);

        return user1;
    }

    @GetMapping("/user/test/update/3")
    @ResponseBody
    @Transactional
    public void updateUse3() {
        // 테스트 회원 "user1" 생성
        createTestUser1();

        // 회원 "user1" 객체 추가
        // 회원 "user1" 을 조회
        User user1 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 이 존재하면,
        if (user1 != null) {
            // 회원의 email 변경
            user1.setEmail("updateUser1@sparta.com");
            // 회원의 role 변경 (USER -> ADMIN)
            user1.setRole(UserRole.ADMIN);
        }

        System.out.println(user1);
        System.out.println(user1.getId());
        System.out.println(user1.getEmail());
        System.out.println(user1.getRole());

        // TODO: DB 에서 테스트 회원 "user1" 데이터 삭제 꼭 직접 해 주세요~!!
    }

    private void createTestUser1() {
        // 회원 "user1" 객체 추가
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setEmail("user1@sprata.com");
        user1.setRole(UserRole.USER);
        // 회원 "user1" 객체를 영속화
        userRepository.save(user1);
    }
}