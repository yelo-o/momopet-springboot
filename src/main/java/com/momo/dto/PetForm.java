package com.momo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class PetForm {

    @NotEmpty(message = "이름 입력은 필수입니다.")
    private String name;

    @NotEmpty(message = "성별 입력은 필수입니다.")
    private String gender;

    @NotEmpty(message = "품종을 입력해주세요.")
    private String breed;

    @NotEmpty(message = "펫 종류를 입력해주세요.")
    private String petType;

    @NotEmpty(message = "생년월일 입력은 필수입니다.")
    private String birthDate;

    @NotEmpty(message = "주의사항을 입력해주세요.")
    private String remark; //주의사항


}
