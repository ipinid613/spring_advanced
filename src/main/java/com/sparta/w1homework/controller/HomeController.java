package com.sparta.w1homework.controller;

import com.sparta.w1homework.security.UserDetailsImpl;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    @GetMapping("/") // @AuthenticationPrincipal -> 스프링 시큐리티가 로그인된 회원의 정보를 넘겨줌.
    public String home(Model model, HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username;
        try {
            username = userDetails.getUsername();
            model.addAttribute("username", username);
//            return "index"; //index를 리턴할 때 username을 포함. 이는 index에서 타임리프로 ${username}하면 연결됨!
        } catch (NullPointerException e) {
            username = "null";
            model.addAttribute("message", null);
//            return "index";
        }
        request.getSession().setAttribute("username", username);
        return "index";
    }

    @GetMapping("/write")
    public String write(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            String username = userDetails.getUsername();
            model.addAttribute("username", username);
            return "write"; //index를 리턴할 때 username을 포함. 이는 index에서 타임리프로 ${username}하면 연결됨!
        } catch (NullPointerException e) {
            model.addAttribute("message", null);
            return "write";
        }
    }

    //관리자만 /admin url을 사용할 수 있다고 지정해주는 것임.
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String admin(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("admin", true);
        return "index";
    }
}