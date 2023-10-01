package com.momo.controller;

import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.domain.member.*;
import com.momo.domain.user.User;
import com.momo.dto.MemberUpdateForm;
import com.momo.dto.PetForm;
import com.momo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;


    Gender gender = null;

    /**
     * 시터 등록 폼으로 이동
     */
    @GetMapping("/members/beSitter")
    public String beSitterForm(Model model) {
        model.addAttribute("memberUpdateForm", new MemberUpdateForm());
        return "members/beSitterForm";
    }

    /**
     * 작성된 form 데이터로 추가 정보 받아서 User 엔티티에 데이터 추가
     */
    @PostMapping("/members/beSitter")
    public String beSitter(@Valid MemberUpdateForm form, BindingResult result,
                           @LoginUser SessionUser user) {

        //memberUpdateForm에 오류가 있는지 확인 (Validation)
        if (result.hasErrors()) {
            return "members/beSitterForm";
        }

        //넘겨줄 정보 정제하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(form.getBirthDate() , formatter);
        Address address = new Address(form.getSi() ,form.getGu());

//        Gender gender = null;
        if (form.getGender().equals("여성")){
            gender = Gender.여성;
        } else {
            gender = Gender.남성;
        }

        //개인정보 객체 생성
        PrivateInformation privateInformation =
                new PrivateInformation(birthDate, address, form.getPhoneNumber(), gender);

        memberService.updateUser(user.getEmail(), privateInformation);

        return "redirect:/members/myInfo";
    }

    /**
     * 유저타입이 null이 아닌 경우 마이페이지로 이동
     */
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

    @GetMapping("/members/myPet")
    public String enrollPetForm(Model model, @LoginUser SessionUser user,
                                PetForm form) {

        //로그인한 유저 정보 받아오고, 받아온 유저 정보로 펫 테이블 조회에서 있는지 없는지 파악
        User findUser = memberService.findOne(user.getEmail());
        model.addAttribute("user", findUser);

        Pet findPet = memberService.findPet(findUser.getId());

        //펫 정보 X => 펫 정보 등록 페이지로 이동
        if (findPet == null) {
            model.addAttribute("form", form);
            return "members/myPetEnrollForm";
        }

        //펫 정보 O => 펫 정보 불러오기
        model.addAttribute("pet", findPet);
        return "members/myPetForm";
    }

    @PostMapping("/members/myPet")

    public String enrollPet(@ModelAttribute("form") @Valid PetForm form, BindingResult result,
                            @LoginUser SessionUser user, Model model) {

        User findUser = memberService.findOne(user.getEmail());
        Pet findPet = memberService.findPet(findUser.getId());

        model.addAttribute("user", findUser);

        //PetEnrollForm에 오류가 있는지 확인 (Validation)
        if (result.hasErrors()) {
            return "members/myPetEnrollForm";
        }

        //넘겨줄 정보 정제하기
//        Gender gender = null;
        if (form.getGender().equals("여성")){
            gender = Gender.여성;
        } else {
            gender = Gender.남성;
        }

        PetType petType = null;
        if (form.getPetType().equals("고양이")){
            petType = PetType.고양이;
        } else {
            petType = PetType.개;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(form.getBirthDate() , formatter);


        //Pet 엔티티에 저장
        memberService.add( Pet.builder()
                .name(form.getName())
                .petType(petType)
                .gender(gender)
                .breed(form.getBreed())
                .birthDate(birthDate)
                .remark(form.getRemark())
                .owner(findUser)
                .build());

        findUser.upgrade(); //SITTER => OWNER 업그레이드

        return "redirect:/members/myPet";
    }

    @GetMapping("members/updateMyPet")
    public String updatePetForm(Model model, @LoginUser SessionUser user,
                                PetForm form) {
        User findUser = memberService.findOne(user.getEmail());
        Pet findPet = memberService.findPet(findUser.getId());

        model.addAttribute("user", findUser);
        model.addAttribute("pet", findPet);
        model.addAttribute("form", form);

        return "members/myPetUpdateForm";
    }

    @PostMapping("members/updateMyPet")
    public String updatePet(@Valid PetForm form, BindingResult result,
                            Model model, @LoginUser SessionUser user) {

        User findUser = memberService.findOne(user.getEmail());
        Pet findPet = memberService.findPet(findUser.getId());

        if (result.hasErrors()) {
//            model.addAttribute("user", findUser);
//            model.addAttribute("pet", findPet);
//            model.addAttribute("form", form);
            return "redirect:/members/updateMyPet";
        }

        //넘겨줄 정보 정제하기
        if (form.getGender().equals("여성")){
            gender = Gender.여성;
        } else {
            gender = Gender.남성;
        }

        PetType petType = null;
        if (form.getPetType().equals("고양이")){
            petType = PetType.고양이;
        } else {
            petType = PetType.개;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(form.getBirthDate() , formatter);

        memberService.updatePet(form.getName(), gender, petType, form.getBreed(),
                birthDate, form.getRemark(), findPet);

        return "redirect:/members/myPet";
    }

    @GetMapping("members/updateMyInfo")
    public String updateInfoForm(Model model, @LoginUser SessionUser user,
                                 MemberUpdateForm form) {

        User findUser = memberService.findOne(user.getEmail());

        model.addAttribute("user", findUser);
        model.addAttribute("form", form);

        return "members/myInfoUpdateForm";
    }

    @PostMapping("members/updateMyInfo")
    public String updateInfo(@ModelAttribute("form") @Valid MemberUpdateForm form, BindingResult result,
                             @LoginUser SessionUser user, Model model) {

        if (result.hasErrors()) {
            User findUser = memberService.findOne(user.getEmail());
            model.addAttribute("user", findUser);
//            model.addAttribute("form", form);
            return "/members/myInfoUpdateForm";
        }

        //넘겨줄 정보 정제하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(form.getBirthDate() , formatter);
        Address address = new Address(form.getSi() ,form.getGu());

//        Gender gender = null;
        if (form.getGender().equals("여성")){
            gender = Gender.여성;
        } else {
            gender = Gender.남성;
        }

        //개인정보 객체 생성
        PrivateInformation privateInformation =
                new PrivateInformation(birthDate, address, form.getPhoneNumber(), gender);

        memberService.updateUserInfo(user.getEmail(), privateInformation);
        return "redirect:/members/myInfo";

    }

}
