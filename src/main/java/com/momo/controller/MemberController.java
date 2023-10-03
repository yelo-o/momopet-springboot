package com.momo.controller;

import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.domain.Item;
import com.momo.domain.member.*;
import com.momo.domain.user.User;
import com.momo.dto.MemberUpdateForm;
import com.momo.dto.PetForm;
import com.momo.service.ItemService;
import com.momo.service.MemberService;
import com.momo.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    private final ItemService itemService;

    private final S3Service s3Service;


    /**
     * String class => LocalDate class 메소드
     */
    private LocalDate toLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }


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
        Gender gender = (form.getGender().equals("여성")) ? Gender.여성 : Gender.남성; //성별
        LocalDate birthDate = toLocalDate(form.getBirthDate()); //날짜
        Address address = new Address(form.getSi() ,form.getGu()); //주소
        String phoneNumber = form.getPhoneNumber();

        //개인정보 객체 생성
        PrivateInformation privateInformation =
                new PrivateInformation(birthDate, address, phoneNumber, gender);

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
    public String enrollPet(@ModelAttribute("form") @Valid PetForm form,
                            BindingResult result, @LoginUser SessionUser user,
                            Model model) throws IOException {

        User findUser = memberService.findOne(user.getEmail());

        model.addAttribute("user", findUser);

        //PetEnrollForm에 오류가 있는지 확인 (Validation)
        if (result.hasErrors()) {
            return "members/myPetEnrollForm";
        }

        //넘겨줄 정보 정제하기
        Gender gender = (form.getGender().equals("여성")) ? Gender.여성 : Gender.남성; //성별
        PetType petType = (form.getPetType().equals("고양이")) ? PetType.고양이 : PetType.개; //애완동물 타입
        LocalDate birthDate = toLocalDate(form.getBirthDate()); //생일

        String name = form.getName();
        String breed = form.getBreed();
        String remark = form.getRemark();

        //사진 url 받기
        String photo = s3Service.uploadFile(form.getPhoto());

        //Pet 엔티티에 저장
        memberService.add( Pet.builder()
                .name(name)
                .petType(petType)
                .gender(gender)
                .breed(breed)
                .birthDate(birthDate)
                .remark(remark)
                .owner(findUser)
                .photo(photo)
                .build());

        findUser.upgrade(); //SITTER => OWNER 업그레이드

        return "redirect:/members/myPet";
    }

    @GetMapping("/members/myItem")
    public String myList(Model model, @LoginUser SessionUser user) {
        User findUser = memberService.findOne(user.getEmail());
        List<Item> items = itemService.findMyItems(user.getEmail());

        model.addAttribute("items", items);
        model.addAttribute("user", findUser);
        return "members/myItemForm";
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
                            Model model, @LoginUser SessionUser user) throws IOException {

        User findUser = memberService.findOne(user.getEmail());
        Pet findPet = memberService.findPet(findUser.getId());

        if (result.hasErrors()) {
            return "redirect:/members/updateMyPet";
        }

        //넘겨줄 정보 정제하기
        Gender gender = (form.getGender().equals("여성")) ? Gender.여성 : Gender.남성; //성별
        PetType petType = (form.getPetType().equals("고양이")) ? PetType.고양이 : PetType.개; //애완동물 타입
        LocalDate birthDate = toLocalDate(form.getBirthDate()); //생일

        String name = form.getName();
        String breed = form.getBreed();
        String remark = form.getRemark();

        //사진 url
        String photo = s3Service.uploadFile(form.getPhoto());

        memberService.updatePet(name, gender, petType, breed,
                birthDate, remark, findPet, photo);

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
    public String updateInfo(@ModelAttribute("form") @Valid MemberUpdateForm form,
                             BindingResult result, @LoginUser SessionUser user, Model model) {

        if (result.hasErrors()) {
            User findUser = memberService.findOne(user.getEmail());
            model.addAttribute("user", findUser);
            return "members/myInfoUpdateForm";
        }

        //넘겨줄 정보 정제하기
        Gender gender = (form.getGender().equals("여성")) ? Gender.여성 : Gender.남성; //성별
        LocalDate birthDate = toLocalDate(form.getBirthDate()); //날짜
        Address address = new Address(form.getSi() ,form.getGu()); //주소
        String phoneNumber = form.getPhoneNumber();

        //개인정보 객체 생성
        PrivateInformation privateInformation =
                new PrivateInformation(birthDate, address, phoneNumber, gender);

        memberService.updateUserInfo(user.getEmail(), privateInformation);
        return "redirect:/members/myInfo";

    }

}
