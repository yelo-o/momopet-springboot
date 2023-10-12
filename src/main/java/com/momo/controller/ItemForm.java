package com.momo.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ItemForm {

    @NotNull(message = "일급 입력은 필수입니다.")
    private Integer price;

    @NotEmpty(message = "자기소개 입력은 필수입니다.")
    private String introduction;

    @NotEmpty(message = "날짜 입력은 필수입니다.")
    private String startDate;

    @NotEmpty(message = "날짜 입력은 필수입니다.")
    private String endDate;
    private Boolean dog;
    private Boolean cat;
    private String si;
    private String gu;
}
