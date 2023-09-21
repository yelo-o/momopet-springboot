package com.momo.controller;

import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    @GetMapping("/members/myPage")
    public String myPage(Model model, @LoginUser SessionUser user) {
        log.info("로그인 이름 : " + user.getName());
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "members/myPage";
    }
}
