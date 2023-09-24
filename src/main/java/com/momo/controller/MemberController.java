package com.momo.controller;

import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.domain.member.Address;
import com.momo.domain.member.Gender;
import com.momo.domain.member.PrivateInformation;
import com.momo.domain.user.Role;
import com.momo.domain.user.User;
import com.momo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/beSitter")
    public String beSitterForm(Model model) {
        model.addAttribute("memberUpdateForm", new MemberUpdateForm());
        return "members/beSitterForm";
    }

//    @GetMapping("/members/update")
//    public String updateForm(Model model, @LoginUser SessionUser user) {
//        log.info("로그인 이름 : " + user.getName());
//        User findUser = memberService.findOne(user.getEmail());
//
//        model.addAttribute("user", findUser);
//
//        model.addAttribute("memberUpdateForm", new MemberUpdateForm());
//        return "members/updateMemberForm";
//    }

    @PostMapping("/members/beSitter")
    public String beSitter(@Valid MemberUpdateForm form, BindingResult result,
                           @LoginUser SessionUser user, @RequestParam("gender") String genderString) {
        //memberUpdateForm에 오류가 있는지 확인 (Validation)
        if (result.hasErrors()) {
            return "members/beSitterForm";
        }

        log.info("변환 전의 성별은 : " + genderString);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(form.getBirthDate() , formatter);
        Address address = new Address(form.getSi() ,form.getGu());

        Gender gender = null;
        if (genderString.equals("여성")){
            gender = Gender.FEMALE;
        } else {
            gender = Gender.MALE;
        }

        log.info("변환 이후의 성별은 : " + gender);

        //개인정보 객체 생성
        PrivateInformation privateInformation =
                new PrivateInformation(birthDate, address, form.getPhoneNumber(), gender);

        memberService.updateUser(user.getEmail(), privateInformation);

        return "redirect:/members/myInfo";
    }
    @GetMapping("/members/myInfo")
    public String myInfoForm(Model model, @LoginUser SessionUser user) {
        User findUser = memberService.findOne(user.getEmail());
        log.info("유저의 권한 : " + findUser.getRole());

        if (findUser.getUserType() == null) {
            return "redirect:/members/beSitter";
        }
        model.addAttribute("user", findUser);

        return "members/myInfoForm";
    }

}
