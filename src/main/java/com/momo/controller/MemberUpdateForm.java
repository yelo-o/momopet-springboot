package com.momo.controller;

import com.momo.domain.member.Address;
import com.momo.domain.member.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class MemberUpdateForm {

    //아래부터 신규로 받아야하는 정보들
    @NotEmpty(message = "생년월일 입력은 필수입니다.")
    private String birthDate;

    @NotEmpty(message = "성별 입력은 필수입니다.")
    private String gender; //FEMALE, MALE

    @NotEmpty(message = "주소 입력(도시)은 필수입니다.")
    private String si;

    @NotEmpty(message = "주소 입력(구)은 필수입니다.")
    private String gu;

    @NotEmpty(message = "전화번호 입력은 필수입니다.")
    private String phoneNumber;
}
