package com.momo.controller;

import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.domain.member.Address;
import com.momo.domain.member.Gender;
import com.momo.domain.member.Pet;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(form.getBirthDate() , formatter);
        Address address = new Address(form.getSi() ,form.getGu());

        Gender gender = null;
        if (form.getGender().equals("여성")){
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
    public String enrollPetFrom(Model model, @LoginUser SessionUser user,
                                PetEnrollForm form) {

        //로그인한 유저 정보 받아오고, 받아온 유저 정보로 펫 테이블 조회에서 있는지 없는지 파악
        User findUser = memberService.findOne(user.getEmail());

        Long findUserId = findUser.getId(); //펫 테이블에 같이 넣을 PK값 불러오기
        Pet findPet = memberService.findPet(findUserId);

        //펫 정보 X => 펫 정보 등록 페이지로 이동
        if (findPet == null) {
            model.addAttribute("user", findUser);
            model.addAttribute("form", form);
            return "members/myPetEnrollForm";
        }

        //펫 정보 O => 펫 정보 불러오기
        model.addAttribute("user", findUser);
        model.addAttribute("pet", findPet);
        return "members/myPetForm";
    }

    @PostMapping("/members/myPet")
    public String enrollPet(@Valid PetEnrollForm form, BindingResult result,
                            @LoginUser SessionUser user) {

        //PetEnrollForm에 오류가 있는지 확인 (Validation)
        if (result.hasErrors()) {
            return "members/myPetEnrollForm";
        }

        //정보 정제하기
        Gender gender = null;
        if (form.getGender().equals("여성")){
            gender = Gender.FEMALE;
        } else {
            gender = Gender.MALE;
        }
        User findUser = memberService.findOne(user.getEmail());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(form.getBirthDate() , formatter);

        //Pet 엔티티에 저장하기
        memberService.add( Pet.builder()
                .name(form.getName())
                .gender(gender)
                .breed(form.getBreed())
                .birthDate(birthDate)
                .remark(form.getRemark())
                .owner(findUser)
                .build());

        //SITTER => OWNER 업데이트


        return "redirect:/members/myPet";
    }

}
