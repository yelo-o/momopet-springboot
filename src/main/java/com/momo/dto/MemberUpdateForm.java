package com.momo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

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
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phoneNumber;
}
