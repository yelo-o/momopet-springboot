package com.momo.controller;

import com.momo.domain.member.Member;
import com.momo.domain.member.PrivateInformation;
import com.momo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Address;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/selectMember")
    public String selectMember() {
        return "members/selectMember";

    }

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        PrivateInformation privateInformation = new PrivateInformation(form.getName(), form.getEmail(), form.getBirthDate(), form.getGender(), form.getAddress(), form.getPhoneNumber());

        Member member = new Member();
        member.setLoginId(form.getLoginId());
        member.setPrivateInformation(privateInformation);

        memberService.join(member);
        return "redirect:/";
    }


}
